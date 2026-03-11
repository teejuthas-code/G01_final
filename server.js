const express = require("express");
const mysql = require("mysql");
require("dotenv").config();
const app = express();

///Hash password
const bcrypt = require("bcryptjs");
const saltRounds = 10;

// Middleware
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use('/uploads', express.static('uploads'));

// MySQL Pool
const db = mysql.createPool({
    connectionLimit: 10,
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    charset: "utf8mb4"
});

// Promise wrapper
function query(sql, params = []) {
    return new Promise((resolve, reject) => {
        db.query(sql, params, (err, results) => {
            if (err) return reject(err);
            resolve(results);
        });
    });
}

//// Register
app.post("/register", async (req, res) => {
    try {
        const { username, user_password, nickname } = req.body;

        if (!username || !user_password || !nickname) {
            return res.status(400).json({
            error: true,
            message: "Username and password are required"
            });
        }

        const cleanUsername = username.trim();

        // กันกรณีพิมพ์เว้นวรรคอย่างเดียว
        if (cleanUsername === "") {
            return res.status(400).json({
                error: true,
                message: "Username cannot be empty"
            });
        }

        //Check if std_id already exists in the system
        const existingUser = await query("SELECT username FROM users WHERE username = ?", 
            [cleanUsername]);
        
        if (existingUser.length > 0) {
            // If data is found, it means the ID is a duplicate
            return res.status(400).json({
                error: true,
                message: "This Username is already in use. Please use another username ."
            });
        }

        //If not a duplicate, start hashing the password
        const hashedPassword = await bcrypt.hash(user_password, saltRounds);

        //Save to database
        await query("INSERT INTO users (username, user_password, nickname, profile_image) VALUES (?, ?, ?, ?)",
        [cleanUsername, hashedPassword,nickname, "default.png"]
        );

        res.status(201).json({
            error: false,
            message: "Registration successful!"
        });

    } catch (err) {
        console.error("Register Error:", err);
        res.status(500).json({
            error: true,
            message: "Internal Server Error"
        });
    }
});

// LOGIN: Verify credentials
app.post("/login", async (req, res) => {
    try {
        const { username, user_password } = req.body;

        if (!username || !user_password) {
            return res.status(400).json({
            error: true,
            message: "Username and password are required"
            });
        }

        const cleanUsername = username.trim();

        if (cleanUsername === "") {
            return res.status(400).json({
                error: true,
                message: "Username cannot be empty"
            });
        }

        //Find User by username
        const users = await query("SELECT * FROM users WHERE username = ?", [cleanUsername]);
        
        if (users.length === 0) {
            return res.status(401).json({
                error: true,
                message: "User not found"
            });
        }

        const user = users[0];
        // Verify password
        const isMatch = await bcrypt.compare(user_password, user.user_password);
        
        if (!isMatch) {
            return res.status(401).json({
                error: true,
                message: "Invalid password"
            });
        }

        //Login Success
        res.json({
            error: false,
            message: "Login successful",
            user_id: user.user_id,
            username: user.username,
            nickname: user.nickname,
            profile_image: user.profile_image
        });

    } catch (err) {
        console.error("Login Error:", err);
        res.status(500).json({
            error: true,
            message: "Internal Server Error"
        });
    }
});

///Profile
app.get("/profile/:id", async (req, res) => {
    try {
        const userId = req.params.id;

        const users = await query(
            "SELECT user_id, username, nickname, profile_image, created_at FROM users WHERE user_id = ?",
            [userId]
        );

        if (users.length === 0) {
            return res.status(404).json({
                error: true,
                message: "User not found"
            });
        }

        const user = users[0];

        res.json({
            error: false,
            user_id: user.user_id,
            username: user.username,
            nickname: user.nickname,
            profile_image: user.profile_image,
            created_at: user.created_at
        });

    } catch (err) {
        res.status(500).json({
            error: true,
            message: "Internal Server Error"
        });
    }
});

//note
app.get("/allNotes", (req, res) => {

    const sql = "SELECT * FROM note WHERE delete_at IS NULL";

    db.query(sql, (err, result) => {
        if (err) {
            return res.status(500).json({ error: err });
        }

        res.json(result);
    });
});

app.delete("/deleteNoteFromCollection", async (req, res) => {

    try {

        const { note_id, collection_id } = req.query;

        console.log("DELETE:", note_id, collection_id);
        await query(
            "DELETE FROM note_collections WHERE note_id = ? AND collection_id = ?",
            [note_id, collection_id]
        );

        res.json({
            error: false,
            message: "Note removed from collection"
        });

    } catch (err) {
        console.log(err);
        res.status(500).json({
            error: true,
            message: err.message
        });
    }
});

app.get("/notesByCollection/:collection_id", async (req, res) => {

    try {

        const collectionId = req.params.collection_id;

        const result = await query(
            `SELECT n.*
             FROM note n
             JOIN note_collections nc
             ON n.note_id = nc.note_id
             WHERE nc.collection_id = ?
             AND n.delete_at IS NULL`,
            [collectionId]
        );

        res.json(result);

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }

});

app.post("/addNoteToCollection", async (req, res) => {

    try {

        const { note_id, collection_id } = req.body;

        if (!note_id || !collection_id) {
            return res.status(400).json({
                error: true,
                message: "Missing data"
            });
        }

        const check = await query(
            "SELECT * FROM note_collections WHERE note_id = ? AND collection_id = ?",
            [note_id, collection_id]
        );

        if (check.length > 0) {
            return res.json({
                error: true,
                message: "Note already in collection"
            });
        }

        await query(
            "INSERT INTO note_collections (note_id, collection_id) VALUES (?, ?)",
            [note_id, collection_id]
        );

        res.json({
            error: false,
            message: "Note added to collection"
        });

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }

});

//get note
app.get("/notes/:user_id", async (req, res) => {
  try {

    const userId = req.params.user_id;

    const notes = await query(
      "SELECT * FROM note WHERE user_id = ? AND delete_at IS NULL",
      [userId]
    );

    res.json(notes);

  } catch (err) {

    res.status(500).json({
      error: true,
      message: err.message
    });

  }
});

/* =========================
   1. ADD TODO
========================= */
app.post("/addTodo", (req, res) => {

    const { user_id, task_text, description, reminder_at } = req.body

    const sql = `
        INSERT INTO todo_list (user_id, task_text, description, is_done, reminder_at)
        VALUES (?, ?, ?, 0, ?)
    `

    db.query(sql, [user_id, task_text, description, reminder_at], (err, result) => {

        if (err) {
            res.status(500).json({ message: "Server error" })
        } else {
            res.json({
                message: "Todo added",
                todo_id: result.insertId
            })
        }

    })

})


/* =========================
   2. GET TODO BY USER
========================= */
app.get("/todos/:user_id", (req, res) => {

    const user_id = req.params.user_id

    const sql = `
        SELECT * FROM todo_list
        WHERE user_id = ?
        AND delete_at IS NULL
    `

    db.query(sql, [user_id], (err, result) => {

        if (err) {
            res.status(500).json({ message: "Server error" })
        } else {
            res.json(result)
        }

    });

});

/* =========================
   3. EDIT TODO
========================= */
app.put("/editTodo/:id", (req, res) => {

    const id = req.params.id
    const { user_id, task_text, description, is_done, reminder_at } = req.body

    const sql = `
        UPDATE todo_list
        SET task_text = ?, 
            description = ?, 
            is_done = ?, 
            reminder_at = ?, 
            update_at = NOW()
        WHERE todo_list_id = ? AND user_id = ?
    `

    db.query(sql, [task_text, description, is_done, reminder_at, id, user_id], (err, result) => {

        if (err) {
            res.status(500).json({ message: "Server error" })
        } 
        else if (result.affectedRows === 0) {
            res.status(404).json({ message: "Todo not found or not your todo" })
        } 
        else {
            res.json({ message: "Todo updated" })
        }

    })

})

/* =========================
   4. DELETE TODO (Soft Delete)
========================= */
app.delete("/deleteTodo/:id", (req, res) => {

    const id = req.params.id

    const sql = `
        UPDATE todo_list
        SET delete_at = NOW()
        WHERE todo_list_id = ?
    `

    db.query(sql, [id], (err, result) => {

        if (err) {
            res.status(500).json({ message: "Server error" })
        } else {
            res.json({ message: "Todo deleted" })
        }

    })

})

// INSERT DATA (POST)
app.post("/insertNote", async (req, res) => {
    try {

        const { user_id, title, content, background_color } = req.body;

        if (!user_id || !title || !content) {
            return res.status(400).json({
                error: true,
                message: "Missing data"
            });
        }

        const result = await query(
            "INSERT INTO note (user_id, title, content, background_color) VALUES (?, ?, ?, ?)",
            [user_id, title, content, background_color]
        );

        res.status(201).json({
            message: "Note created",
            insertId: result.insertId
        });

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }
});

// UPDATE NOTE
app.put("/updateNote/:id", async (req, res) => {
    try {
        const note_id = req.params.id;
        const { title, content, background_color } = req.body;
        const result = await query(
            "UPDATE note SET title = ?, content = ?, background_color = ? WHERE note_id = ?",
            [title, content, background_color, note_id]
        );
        res.json({
            message: "Note updated",
            affectedRows: result.affectedRows
        });
    } catch (err) {
        res.status(500).json({
            error: true,
            message: err.message
        });
    }
});

// SOFT DELETE NOTE
app.put("/deleteNote/:id", async (req, res) => {

    try {

        const note_id = req.params.id;

        const result = await query(
            "UPDATE note SET delete_at = NOW() WHERE note_id = ?",
            [note_id]
        );

        if (result.affectedRows > 0) {

            res.json({
                error: false,
                message: "Note moved to trash"
            });

        } else {

            res.json({
                error: true,
                message: "Note not found"
            });

        }

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }

});


// GET TRASH NOTE
app.get("/note/trash/:user_id", async (req, res) => {

    try {

        const user_id = req.params.user_id;

        const result = await query(
            "SELECT * FROM note WHERE user_id = ? AND delete_at IS NOT NULL ORDER BY delete_at DESC",
            [user_id]
        );

        res.json(result);

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }

});

// RESTORE NOTE
app.put("/note/restore/:id", async (req, res) => {

    try {

        const note_id = req.params.id;

        const result = await query(
            "UPDATE note SET delete_at = NULL WHERE note_id = ?",
            [note_id]
        );

        res.json({
            error: false,
            message: "Note restored"
        });

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }

});

// DELETE NOTE PERMANENT
app.delete("/note/permanent/:id", async (req, res) => {

    try {

        const note_id = req.params.id;

        const result = await query(
            "DELETE FROM note WHERE note_id = ?",
            [note_id]
        );

        res.json({
            error: false,
            message: "Note permanently deleted"
        });

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }

});

// GET NOTE BY USER
app.get("/getNote/:user_id", async (req, res) => {
    try {

        const user_id = req.params.user_id;

        const result = await query(
            "SELECT * FROM note WHERE user_id = ?",
            [user_id]
        );

        res.json(result);

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }
});

app.put("/updateFavorite/:id", async (req, res) => {
    const noteId = req.params.id;
    const isFav = req.query.is_fav;
    try {
        const result = await query(
            "UPDATE note SET is_fav = ? WHERE note_id = ?",
            [isFav, noteId]
        );
        res.json({
            error: false,
            message: "Favorite updated"
        });
    } catch (err) {
        res.status(500).json({
            error: true,
            message: err.message
        });
    }
});

app.put("/updateColor/:id", async (req, res) => {
    const noteId = req.params.id;
    const color = req.query.color;
    try {
        await query(
            "UPDATE note SET background_color = ? WHERE note_id = ?",
            [color, noteId]
        );
        res.json({
            error: false,
            message: "Color updated"
        });
    } catch (err) {
        res.status(500).json({
            error: true,
            message: err.message
        });
    }
});

app.get("/collections/:user_id", async (req, res) => {
    try {

        const userId = req.params.user_id;

        const result = await query(
            "SELECT * FROM collections WHERE user_id = ?",
            [userId]
        );

        res.json(result);

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }
});

// INSERT DATA (POST)
app.post("/addCollection", async (req, res) => {
    try {

        const { user_id, collection_name, background_color } = req.body;

        if (!user_id || !collection_name || !background_color) {
            return res.status(400).json({
                error: true,
                message: "Missing data"
            });
        }

        const result = await query(
            "INSERT INTO collections (user_id, collection_name, background_color) VALUES (?, ?, ?)",
            [user_id, collection_name, background_color]
        );

        res.status(201).json({
            message: "Collection created",
            insertId: result.insertId
        });

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }
});

// update user background
app.put("/user/background/:id", (req, res) => {

    const userId = req.params.id
    const { background } = req.body

    console.log("PUT BACKGROUND CALLED", userId, background)

    const sql = `
        UPDATE users
        SET background = ?
        WHERE user_id = ?
    `

    db.query(sql, [background, userId], (err, result) => {

        if (err) {
            console.error(err)
            return res.status(500).json({ error: "update failed" })
        }

        res.json({
            message: "background updated"
        })
    })
})
// DELETE COLLECTION
app.delete("/deleteCollection/:collection_id", async (req, res) => {
    try {

        const { collection_id } = req.params;

        if (!collection_id) {
            return res.status(400).json({
                error: true,
                message: "collection_id required"
            });
        }

        // ลบ relation ก่อน
        await query(
            "DELETE FROM note_collections WHERE collection_id = ?",
            [collection_id]
        );

        // ลบ collection
        const result = await query(
            "DELETE FROM collections WHERE collection_id = ?",
            [collection_id]
        );

        res.json({
            message: "Collection deleted",
            affectedRows: result.affectedRows
        });

    } catch (err) {

        res.status(500).json({
            error: true,
            message: err.message
        });

    }
});

//planner
app.get("/allPlanner/:user_id", (req, res) => {

    const userId = req.params.user_id;

    const sql = "SELECT * FROM planner WHERE user_id = ? AND delete_at IS NULL";

    db.query(sql, [userId], (err, result) => {
        if (err) {
            return res.status(500).json({ error: err });
        }

        res.json(result);
    });
});

// add planner
app.post("/planner", (req, res) => {

    const {
        user_id,
        planner_content,
        event_start,
        event_end,
        remind
    } = req.body

    const sql = `
    INSERT INTO planner 
    (user_id, planner_content, event_start, event_end, remind) 
    VALUES (?, ?, ?, ?, ?)`

    db.query(
        sql,
        [user_id, planner_content, event_start, event_end, remind],
        (err, result) => {

            if (err) {
                console.log("SQL ERROR =", err)
                return res.status(500).json({ error: err })
            }

            res.json({
                success: true,
                message: "Planner added successfully"
            })
        }
    )
})

// update planner
app.put("/planner/:id", (req, res) => {

    const id = req.params.id
    const {
        planner_content,
        event_start,
        event_end,
        remind,
        is_done
    } = req.body

    const sql = `
    UPDATE planner
    SET 
        planner_content = ?,
        event_start = ?,
        event_end = ?,
        remind = ?,
        is_done = ?,
        update_at = NOW()
    WHERE planner_id = ?
    `

    db.query(sql,
        [planner_content, event_start, event_end, remind, is_done, id],
        (err, result) => {

            if (err) {
                return res.status(500).json({ error: err })
            }

            res.json({ message: "Planner updated" })
        }
    )
})

// mark planner as done
app.put("/plannerDone/:planner_id", (req, res) => {

    const plannerId = req.params.planner_id;

    const sql = "UPDATE planner SET is_done = 1 WHERE planner_id = ?";

    db.query(sql, [plannerId], (err, result) => {

        if (err) {
            return res.status(500).json(err);
        }

        res.json({ message: "planner updated" });

    });

});

// soft delete planner
app.put("/planner/delete/:id", (req, res) => {

    const id = req.params.id

    const sql = "UPDATE planner SET delete_at = NOW() WHERE planner_id = ?"

    db.query(sql, [id], (err, result) => {

        if (err) {
            return res.status(500).json({ error: err })
        }

        res.json({ message: "Planner moved to trash" })
    })

})

// permanent delete planner
app.delete("/planner/permanent/:id", (req, res) => {

    const id = req.params.id

    const sql = "DELETE FROM planner WHERE planner_id = ?"

    db.query(sql, [id], (err, result) => {

        if (err) {
            return res.status(500).json({ error: err })
        }

        res.json({
            message: "Planner permanently deleted"
        })

    })

})


// restore planner
app.put("/planner/restore/:id", (req, res) => {

    const id = req.params.id

    const sql = "UPDATE planner SET delete_at = NULL WHERE planner_id = ?"

    db.query(sql, [id], (err, result) => {

        if (err) {
            return res.status(500).json({ error: err })
        }

        res.json({ message: "Planner restored" })
    })

})

// get deleted planner
app.get("/planner/trash/:user_id", (req, res) => {

    const userId = req.params.user_id

    const sql = "SELECT * FROM planner WHERE user_id = ? AND delete_at IS NOT NULL"

    db.query(sql, [userId], (err, result) => {

        if (err) {
            return res.status(500).json({ error: err })
        }

        res.json(result)

    })

})

// toggle done
app.put("/planner/done/:id", (req, res) => {

    const id = req.params.id
    const { is_done } = req.body

    const sql = `
    UPDATE planner
    SET is_done = ?, update_at = NOW()
    WHERE planner_id = ?
    `

    db.query(sql, [is_done, id], (err, result) => {

        if (err) {
            return res.status(500).json({ error: err })
        }

        res.json({ message: "Planner status updated" })
    })

})






// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
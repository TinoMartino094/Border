# Growing Border

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> A collaborative economy mod that lets your whole server work together to grow the world border — one item at a time.

---

## 🗺️ What Is Growing Border?

**Growing Border** transforms the static world border into a dynamic, community-driven challenge. Players pool together a configurable currency item to fund border expansions. Each dimension tracks its own independent deposit pool, and the border expands by **one full chunk ring (32 blocks)** every time the threshold is met.

The expansion cost scales with the current border size, so early growth is fast and cheap while later expansions require more resources.

---

## Features

- **Per-Dimension Border Economy** — Each dimension (Overworld, Nether, End, etc.) maintains its own independent deposit pool and border size, tracked through persistent `SavedData`.
- **Configurable Currency** — Any item in the game can be configured as the expansion currency (defaults to `minecraft:recovery_compass`).
- **Scalable Cost Curve** — Expansion cost grows with border size using the formula `floor(((4×n) + 4) / chunks_per_item)`, where `n` is the current border size in chunks.
- **Tab-List Progress Display** — Every player sees live border progress (current funds / next threshold) in their tab list footer, updated every second, without any client-side mod required.
- **Server-Side Only** — No client mod needed. Fully compatible with vanilla clients.
- **`/border grow <amount>`** — Players spend items from their inventory to deposit funds toward the next border expansion.
- **`/border coords`** — Displays the current border bounds (min/max X and Z) in chat.
- **Broadcast Announcements** — When the border expands, a server-wide message is broadcast to celebrate the achievement.
- **JSON Config File** — A simple `growing_border.json` config file is auto-generated in the server's config directory on first launch.

---


> **Note:** This mod was developed with AI assistance as a coding aid. All logic has been reviewed and tested, but if you encounter unexpected behavior, please report it via the issue tracker.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE) © 2026 TinoMartino.

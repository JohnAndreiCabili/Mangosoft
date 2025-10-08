# Mangosoft

Mangosoft is an Android application that classifies Philippine mango varieties, assesses quality, and estimates price based on classification results. It combines computer vision and machine learning with a clean, user-friendly interface.

---

## Features

- Upload or capture mango images for classification  
- Identifies mango variety and quality class  
- Displays classification accuracy with percentage bar
- Provides price estimation based on quality
- Live status banner surfaces upload, detection, and pricing progress so you always know what the analyzer is doing
- Intuitive onboarding and guided scanning process

---

## Tech Stack

- Kotlin (Android)  
- Gradle  
- Convolutional Neural Network (CNN) for classification  
- Random Forest Regression for price estimation  

---

## Getting Started

Clone the repository and open it in Android Studio:

```bash
git clone https://github.com/JohnAndreiCabili/Mangosoft.git
```

---

## Running the installable web experience

The `web/` directory contains a Progressive Web App (PWA) port of Mangosoft that you can run and install on macOS, Windows, or Linux. You only need a static file server (Python's built-in server works great) and a modern Chromium, Edge, or Safari (macOS 17+) browser.

- **Fastest path:** `cd web && python3 -m http.server 4173`, then open <http://localhost:4173>.
- **Helper script:** `./scripts/run-web.sh [port]` validates `python3`, launches from `web/`, and defaults to port 4173.
- **Double-click option (macOS):** `web/Run Mangosoft.command` opens Terminal, starts the server, and launches your default browser.

> Need the full walkthrough—including prerequisites, install prompts, troubleshooting tips, and export scripts—in one place? Read [docs/web-quickstart.md](docs/web-quickstart.md).

---

## Exporting the web build to its own GitHub repository

If you only need the installable web version and want to push it to a brand-new GitHub repository, run the helper script included in this repo. It copies everything under `web/` into a fresh folder, initialises a Git history, and leaves you at the point where the only remaining step is to push to GitHub. The script relies on tools that ship with macOS: `git`, `python3`, and `rsync`.

```bash
./scripts/prepare-web-repo.sh
```

The script creates `build/mangosoft-web/` containing the standalone project. From there:

```bash
cd build/mangosoft-web
git remote add origin <your_github_repo_url>
git push -u origin main
```

Once pushed, cloning that new repository on macOS (or any platform) only requires running `python3 -m http.server 4173` inside the cloned folder to launch the web app locally.

---

## Cloning the entire project into a fresh GitHub repository

If you want a clean Git history containing **every** Mangosoft asset (Android app, web port, scripts, and documentation), use the companion helper script. It mirrors the whole repository—minus the existing `.git` folder and build artifacts—into `build/mangosoft-full/`, initialises a new Git repository, and makes the first commit for you.

```bash
./scripts/prepare-full-repo.sh
```

After the script finishes:

```bash
cd build/mangosoft-full
git remote add origin <your_github_repo_url>
git push -u origin main
```

At that point the new GitHub repository is ready for collaborators to clone and run either the Android project (via Android Studio) or the installable web build (with `cd web && python3 -m http.server 4173`).

---

## Managing generated exports

Both helper scripts write their output to the `build/` directory and are safe to run repeatedly. The generated folders are ignored by Git so the main branch stays lightweight and free of binary assets. If you want to keep a snapshot alongside the source, rerun the relevant script and copy the resulting folder wherever you intend to publish it. The `distribution/` folder now documents this workflow without tracking large binary files.

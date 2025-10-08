# Mangosoft Web Quick Start

Mangosoft Web is a Progressive Web App (PWA) that mirrors the Android experience: upload or capture a mango photo, receive the AI classification, view the confidence score and quality class, and get a price estimate. Follow the steps below to run it locally on macOS, Windows, or Linux and optionally install it as a desktop app.

---

## TL;DR

```bash
cd web
python3 -m http.server 4173
```

Then browse to <http://localhost:4173>. The rest of this guide expands on prerequisites, platform-specific tips, and troubleshooting.

Prefer a helper? From the project root run `./scripts/run-web.sh [port]` (defaults to `4173`) or double-click `web/Run Mangosoft.command` on macOS. Both options verify `python3`, launch the static server from `web/`, and open your browser to the correct URL.

---

## 1. Install prerequisites (if needed)

- **Python 3.8+**
  - macOS ships with Python 3. Update with `brew install python` if desired.
  - On Windows, install Python from <https://www.python.org/downloads/> and check **Add python.exe to PATH**.
  - Most Linux distributions already include Python 3; otherwise install it with your package manager (e.g., `sudo apt install python3`).
- **Modern browser** with PWA support (Chrome 113+, Edge 113+, or Safari 17+ on macOS).

---

## 2. Start the local server

From the repository root run the command that matches your platform:

<details>
<summary><strong>macOS / Linux (Terminal)</strong></summary>

```bash
cd /path/to/Mangosoft/web
python3 -m http.server 4173
```

</details>

<details>
<summary><strong>Windows (PowerShell)</strong></summary>

```powershell
cd C:\path\to\Mangosoft\web
py -m http.server 4173
```

</details>

If port `4173` is busy, choose another port (for example `8080`) and use that value when opening your browser.

---

## 3. Open the app in your browser

Visit <http://localhost:4173>. The splash screen will appear, followed by the onboarding carousel. You can now upload images, capture new photos (grant camera access when prompted), and view the AI-generated classification results.

---

## 4. Install the PWA (optional)

1. With the app open at `http://localhost:4173`, look for the install prompt:
   - **Chrome / Edge:** Click the install icon (+) in the address bar.
   - **Safari (macOS 17+):** Use **Share → Add to Dock**.
2. Confirm the prompt. The app now appears in your application launcher and opens in its own window. Cached assets mean the UI loads instantly even when offline (API calls still require connectivity).
3. Whenever you change files in `web/`, refresh the page so the service worker can fetch the latest assets. Reinstalling is not necessary.

---

## 5. Stop the server

Return to the Terminal/PowerShell window and press `Ctrl + C` to shut down the local server when you're finished.

---

## Troubleshooting

- **Blank page after updating files:** Refresh twice to bust the service worker cache, or run `navigator.serviceWorker.getRegistrations().then(regs => regs.forEach(reg => reg.unregister()))` from DevTools and reload.
- **Still seeing a status banner?** The banner steps through **Uploading → Detecting → Estimating**. If the APIs take longer than nine seconds you'll see a sample mango result with a reconnect notice—start a new scan once you're back online for live predictions.
- **Camera button disabled:** Ensure your browser has camera permissions and that you're running over `http://localhost` or `https://` (required for `getUserMedia`).
- **Port already in use:** Pass a different port to the `http.server` command and update the browser URL accordingly.
- **Windows firewall prompt:** Allow Python to communicate on private networks so the local browser can connect.

---

## Exporting to a standalone GitHub repository

To publish only the installable web build, run the helper script from the project root:

```bash
./scripts/prepare-web-repo.sh
```

It copies everything under `web/` into `build/mangosoft-web/`, initializes a fresh Git history, and makes the initial commit. From there run:

```bash
cd build/mangosoft-web
git remote add origin <your_github_repo_url>
git push -u origin main
```

Cloning that new repository on macOS (or any platform) and running `python3 -m http.server 4173` inside the cloned folder is all you need to launch the web app locally.

Need the entire project instead (Android + web)? Use `./scripts/prepare-full-repo.sh` to mirror the full repository into `build/mangosoft-full/` with a clean Git history.

---

## When the analyzer is offline

If the hosted APIs time out or you're offline, Mangosoft Web surfaces a branded sample result after a few seconds so you can continue exploring the UI. A blue notice appears on the result card explaining that the live analyzer is reconnecting, and the status banner shows the reconnect message briefly. Start another scan once you're back online and the banner will move through the live stages again.


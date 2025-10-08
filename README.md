# Mangosoft

Mangosoft is an Android application that classifies Philippine mango varieties, assesses quality, and estimates price based on classification results. It combines computer vision and machine learning with a clean, user-friendly interface.

---

## Features

- Upload or capture mango images for classification  
- Identifies mango variety and quality class  
- Displays classification accuracy with percentage bar  
- Provides price estimation based on quality  
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

**In a hurry?** From the repository root run `cd web && python3 -m http.server 4173`, then open <http://localhost:4173> in your browser. That’s all you need to see the web app. The expanded instructions below cover prerequisites, installation prompts, and troubleshooting in more detail if you run into issues.

The `web/` directory contains a Progressive Web App (PWA) port of Mangosoft that you can run and install on macOS, Windows, or Linux. You only need a static file server (Python's built-in server works great) and a modern Chromium, Edge, or Safari (macOS 17+) browser.

### 1. Install prerequisites (if needed)

- **Python 3.8+**
  - macOS ships with Python 3. To upgrade, run `brew install python`.
  - On Windows, install Python from <https://www.python.org/downloads/> and be sure to check **Add python.exe to PATH**.
  - Most Linux distributions already include Python 3; otherwise install it with your package manager (e.g., `sudo apt install python3`).
- **Modern browser** with PWA support (Chrome 113+, Edge 113+, or Safari 17+ on macOS).

### 2. Start the local server

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

If port `4173` is busy, pick another port (e.g., `8080`) and use the same port number when opening the browser.

### 3. Open the app in your browser

Navigate to <http://localhost:4173>. The splash screen will appear, followed by the onboarding carousel. You can now upload images, capture new photos (allow camera access when prompted), and view the AI-generated classification results.

### 4. Install the PWA (optional but recommended)

1. With the app open at `http://localhost:4173`, look for the **Install Mangosoft** prompt:
   - **Chrome / Edge:** Click the install icon (+) in the address bar.
   - **Safari (macOS 17+):** Use the **Share → Add to Dock** menu item.
2. Confirm the prompt. The app now appears in your application launcher and opens in its own window. Cached assets mean the UI loads instantly even when offline (API calls still need an internet connection).

Whenever you change files inside `web/`, refresh the page once so the service worker can download the latest assets. Reinstalling is not required.

### 5. Stop the server

Return to the Terminal/PowerShell window and press `Ctrl + C` to shut down the local server when you are done.

### Troubleshooting

- **Blank page after updating files:** Refresh twice to bust the service worker cache, or run `navigator.serviceWorker.getRegistrations().then(regs => regs.forEach(reg => reg.unregister()))` from DevTools and reload.
- **Camera button disabled:** Make sure your browser has camera permissions and that you're running over `http://localhost` or `https://` (required for getUserMedia).
- **Port already in use:** Pass a different port to the `http.server` command and update the browser URL accordingly.
- **Windows firewall prompt:** Allow Python to communicate on private networks so the local browser can connect.

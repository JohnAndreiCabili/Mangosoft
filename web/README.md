# Mangosoft Web

A browser-based port of the Mangosoft Android application. The web experience mirrors the native flow: splash and onboarding screens lead to scanning options where users can upload or capture mango photos, receive AI-powered classification, view confidence and quality class, and get a price estimate.

## Getting Started

You only need a static file server to run the web build.

### Quick start on macOS

1. Open the **Terminal** application (`⌘ + Space`, type "Terminal").
2. Navigate into the project folder and start the built-in Python web server:

   ```bash
   cd /path/to/Mangosoft/web
   python3 -m http.server 4173
   ```

   macOS ships with Python 3, but if you have [Homebrew](https://brew.sh/) installed you can also run `brew install python` to
   get the latest version.
3. Open your browser to <http://localhost:4173>.
4. The first time you try to capture an image, macOS will prompt you to allow camera access. Accept the prompt to use the
   in-browser camera capture.

When you're done, return to Terminal and press `Ctrl + C` to stop the server.

### Other platforms

From any operating system you can start a static server by running:

```bash
# From the repository root
cd web
python -m http.server 4173
```

Then open your browser to <http://localhost:4173>.

> **Tip:** Any static hosting service (GitHub Pages, Netlify, Firebase Hosting, etc.) can serve the contents of this folder without additional build steps.

### Install as a desktop app (Progressive Web App)

Mangosoft Web is a Progressive Web App, so once it is running in your browser you can install it like a native application:

1. Start the local server (see instructions above) and open <http://localhost:4173> in **Chrome**, **Edge**, or **Safari (macOS 17+)**.
2. Look for the **Install** or **Add to Dock** icon in the address bar and follow the prompt to install Mangosoft.
3. After installation the app appears in your OS launcher and opens in its own window. Cached assets mean the UI loads instantly even when offline (API calls still require connectivity).

If you update the code, refresh the page once to allow the service worker to download the latest files before reinstalling.

## Environment Variables

The application targets the same hosted APIs as the Android app:

- CNN classification: `https://mangosoft-722758638200.asia-southeast1.run.app/predict`
- Random Forest price estimation: `https://mangosoft-e87cfb72dc61.herokuapp.com/predict`

If you need to point to alternative deployments, update the `BASE_URL_YOLO` and `BASE_URL_RFR` constants inside `web/main.js`.

## Saving Results

The **Save a Copy** button uses [`html2canvas`](https://html2canvas.hertzen.com/) from a CDN to export the result card. The feature requires network access the first time it runs so the script can be downloaded. Once cached by the browser, it continues to work offline.

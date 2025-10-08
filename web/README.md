# Mangosoft Web

A browser-based port of the Mangosoft Android application. The web experience mirrors the native flow: splash and onboarding screens lead to scanning options where users can upload or capture mango photos, receive AI-powered classification, view confidence and quality class, and get a price estimate.

While a scan is running the app now displays a live banner that walks through each stage—uploading your mango photo, detecting the variety, and estimating the quality and price—so you always know what is happening behind the scenes.

## Getting Started

**Need the 10-second version?** Run `cd web && python3 -m http.server 4173` from the repo root, then browse to <http://localhost:4173>. Prefer automation? `../scripts/run-web.sh [port]` (or `./scripts/run-web.sh [port]` from the root) checks for `python3`, starts the server, and defaults to port 4173, while macOS users can double-click `Run Mangosoft.command`.

All platform-specific setup notes, installation prompts, troubleshooting guidance, and export instructions now live in [docs/web-quickstart.md](../docs/web-quickstart.md) so there is a single source of truth that avoids merge conflicts.

### One-command export to a standalone GitHub repo

From the repository root run:

```bash
./scripts/prepare-web-repo.sh
```

The script copies the contents of this folder into `build/mangosoft-web/`, initialises a fresh Git history, and commits everything so you can immediately add a GitHub remote and push. It uses tools that ship with macOS (`git`, `python3`, and `rsync`). Once the new repository is created, cloning it and running `python3 -m http.server 4173` is all that is required to launch the app locally.

> **Need every Mangosoft asset (Android + web) in a brand-new repo instead?** Run `./scripts/prepare-full-repo.sh` from the root to clone the complete project into `build/mangosoft-full/` with a clean Git history.

## Environment Variables

The application targets the same hosted APIs as the Android app:

- CNN classification: `https://mangosoft-722758638200.asia-southeast1.run.app/predict`
- Random Forest price estimation: `https://mangosoft-e87cfb72dc61.herokuapp.com/predict`

If you need to point to alternative deployments, update the `BASE_URL_YOLO` and `BASE_URL_RFR` constants inside `web/main.js`.

## Live analysis banner

The status banner at the top of the interface steps through three stages for every scan:

1. **Uploading your mango photo…**
2. **Detecting mango variety…**
3. **Estimating quality and price…**

If the APIs pause for more than nine seconds, the banner switches to a reconnect notice while the UI surfaces a sample mango profile. Start another scan once you're back online and the banner will move through the live stages again.

## Saving Results

The **Save a Copy** button uses [`html2canvas`](https://html2canvas.hertzen.com/) from a CDN to export the result card. The feature requires network access the first time it runs so the script can be downloaded. Once cached by the browser, it continues to work offline.

### When the analyzer is offline

If the hosted APIs time out or you are disconnected from the internet, Mangosoft Web now surfaces a branded sample result after a few seconds so you can keep exploring the interface. A blue notice appears on the result card explaining that the live analyzer is reconnecting, and the status banner shows the reconnect message for a short time. You can retry the scan at any time from the **Scan Again** button. Once the services respond again, real predictions automatically replace the sample output and the banner returns to its normal stage progression.

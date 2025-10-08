# Distribution Exports

This folder documents how to generate distributable snapshots of the Mangosoft project without checking large binary assets into the main repository. Use the helper scripts in `../scripts/` whenever you need a fresh copy:

- `../scripts/prepare-web-repo.sh` → produces `build/mangosoft-web/`, a standalone copy of the installable web experience.
- `../scripts/prepare-full-repo.sh` → produces `build/mangosoft-full/`, a full export of the Android and web projects with a clean Git history.

The generated folders live under `build/` and are gitignored by default. After running a script you can `cd` into the output directory, add a GitHub remote, and push. If you prefer to archive the exports elsewhere, copy the generated folder to your destination of choice.

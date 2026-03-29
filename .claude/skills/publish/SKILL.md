---
name: publish
description: Initialize git, verify .gitignore, commit, create a public GitHub repo under dimashyshkin, and push. Run this when the project has no remote yet.
---

You are publishing this project to GitHub for the first time. Work through the steps below in order.

## Step 0 — Locate gh on Windows

`gh` is not on bash's PATH in this environment. Use the full path:

```
GH="/c/Program Files/GitHub CLI/gh.exe"
```

Verify auth before proceeding:

```bash
"$GH" auth status
```

If not authenticated, tell the user to run `gh auth login` in a terminal and stop.

## Step 1 — Check git state

```bash
git -C . rev-parse --is-inside-work-tree 2>/dev/null || echo "not a repo"
```

- If already a repo with a remote, confirm with the user before doing anything.
- If already a repo but no remote, skip to Step 3.
- If not a repo, proceed to Step 2.

## Step 2 — Review and update .gitignore

Read the current `.gitignore`. Make sure it excludes at minimum:

| Path | Reason |
|------|--------|
| `target/` | Maven build output |
| `.idea/` | IntelliJ workspace files |
| `*.iml` | IntelliJ module files |
| `.allure/` | Local Allure CLI binary |
| `.claude/settings.local.json` | Personal permission overrides |
| `.vscode/settings.local.json` | Local VS Code overrides |

Do NOT exclude:
- `.mcp.json` — project MCP config, useful for contributors
- `.claude/skills/` — project-specific skills
- `.claude/launch.json` — debug launch config
- `.vscode/settings.json` — non-sensitive project settings
- `CLAUDE.md` — project instructions

Add any missing entries, then run `git init`.

## Step 3 — Stage and inspect

```bash
git add .
git status
```

Review the staged file list. If anything sensitive or oversized appears (secrets, large binaries, personal data), unstage it and update `.gitignore` before continuing.

## Step 4 — Commit

```bash
git commit -m "$(cat <<'EOF'
Initial commit: <one-line project description>

<optional second paragraph with key tech/purpose>

Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>
EOF
)"
```

## Step 5 — Create repo and push

Use the repo name from the project directory unless the user specified otherwise.

```bash
GH="/c/Program Files/GitHub CLI/gh.exe"
"$GH" repo create <repo-name> \
  --public \
  --description "<short description>" \
  --source=. \
  --remote=origin \
  --push
```

## Step 6 — Confirm

Print the repo URL returned by gh. Tell the user what was pushed and what was excluded.

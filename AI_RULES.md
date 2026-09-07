# AI DEVELOPMENT RULES

## Before modifying code
Always:
1. Read AI_RULES.md
2. Read PROJECT_HANDOFF.md
3. Read TASKS.md
4. Read CURRENT_SESSION.md
5. Inspect the source files relevant to the current task
6. Check git status and recent git history

Never begin coding from task text alone.

## General engineering rules
- This is an existing production-style project, not a prototype.
- Prefer the smallest coherent fix.
- Fix root causes instead of applying visual patches.
- Do not perform unrelated refactors during a scoped task.
- Do not silently change persistence contracts.
- Do not delete migration logic.
- Do not change JSON structures without an explicit migration.
- Do not change widget provider declarations unless the task requires it.
- Do not reintroduce placeholder text or hard-coded sample content.
- Do not remove working features to make another task easier.
- Preserve backward compatibility with existing saved projects where reasonably possible.
- Keep editor preview behavior and actual Android widget rendering consistent.
- Avoid duplicating layout or transformation logic across editor and renderer.

## Build rules
After every code task:
1. Run assembleDebug
2. Fix compilation errors
3. Review git diff
4. Check for accidental unrelated changes
5. Update TASKS.md
6. Update CURRENT_SESSION.md
7. Commit with a descriptive commit message

Build command (PowerShell, from project root):

```powershell
$env:JAVA_HOME = "d:\Users\JCLXJ\Documents\Android Development\.tooling\jdk\jdk-17.0.20.1+1"
$env:ANDROID_HOME = "d:\Users\JCLXJ\Documents\Android Development\.android-sdk"
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
.\gradlew.bat assembleDebug
```

## Commit rules
One scoped task should normally correspond to one focused commit.

Use messages such as:
```
fix(editor): support unlimited rotation and correct drag direction
fix(text): improve multiline line spacing
refactor(editor): unify rotation handles
feat(editor): add equal-size snapping guides
feat(shapes): rebuild shape geometry library
feat(fonts): curate bundled font library
feat(icons): refresh solar icon assets
feat(ui): add polished editor transitions
```

Avoid messages like: `update app`, `fix stuff`, `changes`

## Code quality rules
- Do not turn MainActivity.kt into an even larger monolith if reusable logic can be extracted safely.
- However, do not perform large architectural extraction during a bug fix unless necessary.
- Prefer reusable helpers/components over three separate implementations of the same behavior.
- Prefer one shared rotation handle implementation for component/city/text.
- Prefer one snapping engine instead of separate city/text/component snapping code.
- Prefer one coordinate transformation model instead of per-feature coordinate math.

## Regression protection
Do not break:
- existing project JSON loading
- layout migrations
- widget-instance persistence
- empty project support
- city timezone behavior
- date/time rendering
- day/night calculation
- widget pin/add flow
- widget receiver declarations
- SVG asset loading
- existing project compatibility
- app ability to build

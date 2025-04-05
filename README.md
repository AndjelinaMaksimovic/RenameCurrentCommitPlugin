# RenameCurrentCommitPlugin

`RenameCurrentCommitPlugin` is an IntelliJ IDEA plugin that allows users to **rename the latest Git commit message** directly from the IDE.

## Installation

1. Download the ZIP file located in: /distributions/RenameCurrentCommit-Plugin.zip
2. Open **IntelliJ IDEA**.
3. Navigate to **Settings > Plugins**.
4. Click the ⚙️ **gear icon** > **Install Plugin from Disk...**
5. Select the ZIP file mentioned in first step.
6. Restart IntelliJ IDEA if prompted.

The plugin can then be accessed from the **Git** menu group, or by searching for **Rename Current Commit**.

## Edge Cases Covered

This plugin handles several scenarios to ensure safe operation:

### 1. Valid Project & Git Repo Checks
- If **no project** is open, the plugin will display an error.
- If the project is **not a Git repository**, the plugin will notify the user.

### 2. Empty Repository
- If the repository **has no commits yet**, the plugin will inform the user that there’s nothing to rename.

### 3. Pushed Commits
- If the latest commit **has already been pushed**, the plugin will display a warning that renaming will change the commit history, and the user may need to use `--force` when pushing the commit later.

### 4. User Cancellation
- If the user cancels the commit renaming input prompt, the operation will be safely aborted.

## Running the Plugin in Test Mode

To run the plugin directly from source, follow these steps:

1. **Clone the repository**:

git clone https://github.com/your-org/rename-current-commit-plugin.git
cd rename-current-commit-plugin

2. **Open the Project in IntelliJ IDEA**
- Open **IntelliJ IDEA** and select **File > Open**. Choose the cloned project folder.
- If the **IntelliJ Platform Plugin SDK** is not set up, IntelliJ will prompt you to do so. Set it up via **File > Project Structure > Project Settings > SDK** and select **IntelliJ Platform SDK**.

3. **Build the Plugin (Optional)**
- Use the **Gradle** or **Maven** tab to build the project, or manually build it from the **Build** menu in IntelliJ.

4. **Run the Plugin in a Development Instance**
- Go to **Run > Run Plugin** in the top menu. This will open a new IntelliJ instance with the plugin loaded in sandbox mode for testing.

5. **Test the Plugin**
- Open any **Git repository** in the sandbox instance.
- Go to **Git > Rename Current Commit** and try renaming the commit message.

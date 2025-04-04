package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.jetbrains.annotations.NotNull;
import services.GitService;

import java.io.IOException;

public class RenameCurrentCommitAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

//        Messages.showInfoMessage(project, "Rename current commit action", "Info");

        try {
            Repository repository = GitService.getValidRepository(project);
            try (Git git = new Git(repository)) {
                String currentCommitMessage = GitService.getCurrentCommitMessage(repository);
                String commitId = GitService.getCurrentCommitHash(repository);
                boolean isPushed = GitService.isCommitPushed(git, commitId);

                if (!confirmCommitRename(currentCommitMessage, isPushed)) {
                    return;
                }

                String newCommitName = Messages.showInputDialog("Enter new commit name:", "New Commit Name", null);
                if (newCommitName != null) {
                    GitService.renameCommit(git, newCommitName);
                }

            }
        } catch (IllegalArgumentException ex) {
            Messages.showErrorDialog(ex.getMessage(), "Git Error");
        } catch (IOException ex) {
            if (ex.getMessage().contains("no commits") || ex.getMessage().contains("No commits")) {
                Messages.showWarningDialog("There are no commits in this repository yet. Nothing to rename.", "No Commits Found");
            } else {
                Messages.showErrorDialog(ex.getMessage(), "Git Error");
            }
        }catch (GitAPIException ex) {
             Messages.showErrorDialog("Can't determine push status. Commit rename aborted.", "Git Error");
        }
    }

    private boolean confirmCommitRename(String currentCommitMessage, boolean isPushed) {
        String message = "Are you sure you want to rename the current commit from: \"" + currentCommitMessage + "\"?";
        String title = "Renaming Confirmation";

        if (isPushed) {
            message = "<html><b><font color='red'>WARNING:</font></b> This commit has already been pushed! Changing its name will change history, and you may need to use <code>--force</code> when pushing.<br><br>" + message + "</html>";
        }

        int result = Messages.showDialog(message, title, new String[]{"Yes", "No"}, 0, Messages.getQuestionIcon());
        return result == 0;
    }


}
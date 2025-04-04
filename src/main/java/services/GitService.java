package services;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GitService {

    public static Repository getValidRepository(Project project) throws IllegalArgumentException, IOException {

        validateProject(project);
        File projectDir = new File(project.getBasePath());
        validateGitRepository(projectDir);

        try {
            Repository repository = new RepositoryBuilder()
                    .setWorkTree(projectDir)
                    .readEnvironment()
                    .findGitDir()
                    .build();

            return repository;

        } catch (IOException e) {
            throw new IOException("Failed to open Git repository: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No Git repository found in project: " + projectDir.getAbsolutePath(), e);
        }

    }


    // checking if any project is open in IntelliJ and correctly open
    private static void validateProject(Project project) {

        if (project == null || project.getBasePath() == null) {
            throw new IllegalArgumentException("No valid project detected. Please open a project before running this action.");
        }

    }


    // checking if the open project is actually a git repo
    private static void validateGitRepository(File projectDir) throws IllegalArgumentException{

        if (!projectDir.exists() || !projectDir.isDirectory()) {
            throw new IllegalArgumentException("The project directory does not exist or is not accessible: " + projectDir.getAbsolutePath());
        }

        // trying to find git repo in the current directory
        File gitDir = new File(projectDir, ".git");
        if (!gitDir.exists() || !gitDir.isDirectory()) {
            throw new IllegalArgumentException("No .git directory found in the project. Is this really a Git repository?");
        }
    }

    public static String getCurrentCommitMessage(Repository repository) throws IOException {
        try (RevWalk revWalk = new RevWalk(repository)) {
            var headId = repository.resolve("HEAD");

            if (headId == null) {
                throw new IOException("There are no commits in the repository yet.");
            }

            return revWalk.parseCommit(headId).getFullMessage();
        }
    }

    public static String getCurrentCommitHash(Repository repository) throws IOException {
        return repository.resolve("HEAD").name();
    }

    public static boolean isCommitPushed(Git git, String commitId) throws GitAPIException {
        List<Ref> branches = git.branchList()
                .setContains(commitId)
                .setListMode(ListBranchCommand.ListMode.REMOTE)
                .call();
        return !branches.isEmpty();
    }

    public static void renameCommit(Git git, String newMessage) throws GitAPIException {
        git.commit()
                .setAmend(true)
                .setMessage(newMessage)
                .call();
    }

}

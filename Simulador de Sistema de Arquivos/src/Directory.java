import java.util.ArrayList;
import java.util.List;

public class Directory {
    private String path;
    private List<File> files;
    private List<Directory> subdirectories;

    public Directory(String path) {
        this.path = path;
        this.files = new ArrayList<>();
        this.subdirectories = new ArrayList<>();
    }

    public void setPath(String newPath) {
        this.path = newPath;
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void removeFile(String fileName) {
        files.removeIf(f -> f.getName().equals(fileName));
    }

    public void renameFile(String oldName, String newName) {
        for (File file : files) {
            if (file.getName().equals(oldName)) {
                file.setName(newName);
                break;
            }
        }
    }

    public File getFile(String fileName) {
        for (File file : files) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    public void addSubdirectory(Directory directory) {
        subdirectories.add(directory);
    }

    public void removeSubdirectory(String dirName) {
        subdirectories.removeIf(d -> d.getPath().equals(dirName));
    }

    public Directory getSubdirectory(String dirName) {
        for (Directory dir : subdirectories) {
            if (dir.getPath().equals(dirName)) {
                return dir;
            }
        }
        return null;
    }

    public String getPath() {
        return path;
    }

    public void listContents() {
        System.out.println("Contents of directory " + path + ":");
        for (Directory dir : subdirectories) {
            System.out.println(" - " + dir.getPath());
        }
        for (File file : files) {
            System.out.println(" - " + file.getName());
        }
    }
}

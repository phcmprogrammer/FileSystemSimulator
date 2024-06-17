import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileSystemSimulator {
    private Map<String, Directory> directories;
    private Journal journal;

    public FileSystemSimulator(String journalFileName) {
        directories = new HashMap<>();
        journal = new Journal(journalFileName);
        directories.put("/", new Directory("/"));
    }

    public void createDirectory(String path) {
        journal.log("CREATE_DIRECTORY", path);
        String parentPath = getParentPath(path);
        Directory parentDir = directories.get(parentPath);
        if (parentDir != null) {
            Directory newDir = new Directory(path);
            directories.put(path, newDir);
            parentDir.addSubdirectory(newDir);
        } else {
            System.out.println("Parent directory not found: " + parentPath);
        }
    }

    public void deleteDirectory(String path) {
        journal.log("DELETE_DIRECTORY", path);
        Directory dir = directories.get(path);
        if (dir != null) {
            String parentPath = getParentPath(path);
            Directory parentDir = directories.get(parentPath);
            if (parentDir != null) {
                parentDir.removeSubdirectory(path);
            }
            directories.remove(path);
        } else {
            System.out.println("Directory not found: " + path);
        }
    }

    public void renameDirectory(String oldPath, String newPath) {
        journal.log("RENAME_DIRECTORY", oldPath, newPath);
        Directory dir = directories.remove(oldPath);
        if (dir != null) {
            String parentPath = getParentPath(oldPath);
            Directory parentDir = directories.get(parentPath);
            if (parentDir != null) {
                parentDir.removeSubdirectory(oldPath);
                dir.setPath(newPath);
                parentDir.addSubdirectory(dir);
                directories.put(newPath, dir);
            }
        } else {
            System.out.println("Directory not found: " + oldPath);
        }
    }

    public void listDirectory(String path) {
        Directory dir = directories.get(path);
        if (dir != null) {
            dir.listContents();
        } else {
            System.out.println("Directory not found: " + path);
        }
    }

    public void createFile(String dirPath, String fileName) {
        journal.log("CREATE_FILE", dirPath, fileName);
        Directory dir = directories.get(dirPath);
        if (dir != null) {
            dir.addFile(new File(fileName));
        } else {
            System.out.println("Directory not found: " + dirPath);
        }
    }

    public void deleteFile(String dirPath, String fileName) {
        journal.log("DELETE_FILE", dirPath, fileName);
        Directory dir = directories.get(dirPath);
        if (dir != null) {
            dir.removeFile(fileName);
        } else {
            System.out.println("Directory not found: " + dirPath);
        }
    }

    public void renameFile(String dirPath, String oldName, String newName) {
        journal.log("RENAME_FILE", dirPath, oldName, newName);
        Directory dir = directories.get(dirPath);
        if (dir != null) {
            dir.renameFile(oldName, newName);
        } else {
            System.out.println("Directory not found: " + dirPath);
        }
    }

    public void copyFile(String sourceDirPath, String destDirPath, String fileName) {
        journal.log("COPY_FILE", sourceDirPath, destDirPath, fileName);
        Directory sourceDir = directories.get(sourceDirPath);
        Directory destDir = directories.get(destDirPath);
        if (sourceDir != null && destDir != null) {
            File file = sourceDir.getFile(fileName);
            if (file != null) {
                destDir.addFile(new File(file.getName()));
            } else {
                System.out.println("File not found: " + fileName);
            }
        } else {
            System.out.println("Directory not found.");
        }
    }

    public void close() {
        journal.close();
    }

    private String getParentPath(String path) {
        int lastSlashIndex = path.lastIndexOf('/');
        return lastSlashIndex > 0 ? path.substring(0, lastSlashIndex) : "/";
    }
    
    public void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  mkdir [path]           - Create a directory");
        System.out.println("  rmdir [path]           - Remove a directory");
        System.out.println("  renameDir [oldPath] [newPath] - Rename a directory");
        System.out.println("  ls [path]              - List contents of a directory (defaults to root if no path is specified)");
        System.out.println("  createFile [dirPath] [fileName] - Create a file");
        System.out.println("  deleteFile [dirPath] [fileName] - Delete a file");
        System.out.println("  renameFile [dirPath] [oldName] [newName] - Rename a file");
        System.out.println("  copyFile [sourceDirPath] [destDirPath] [fileName] - Copy a file");
        System.out.println("  exit                   - Close the journal and exit the program");
        System.out.println("  help                   - Show this help message");
    }
    

    public static void main(String[] args) {
        FileSystemSimulator fs = new FileSystemSimulator("journal.txt");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();
            String[] parts = command.split(" ");
            if (parts.length == 0) continue;

            switch (parts[0]) {
                case "mkdir":
                    if (parts.length > 1) {
                        fs.createDirectory(parts[1]);
                    } else {
                        System.out.println("Usage: mkdir [path]");
                    }
                    break;
                case "rmdir":
                    if (parts.length > 1) {
                        fs.deleteDirectory(parts[1]);
                    } else {
                        System.out.println("Usage: rmdir [path]");
                    }
                    break;
                case "renameDir":
                    if (parts.length > 2) {
                        fs.renameDirectory(parts[1], parts[2]);
                    } else {
                        System.out.println("Usage: renameDir [oldPath] [newPath]");
                    }
                    break;
                case "ls":
                    if (parts.length > 1) {
                        fs.listDirectory(parts[1]);
                    } else {
                        fs.listDirectory("/");
                    }
                    break;
                case "createFile":
                    if (parts.length > 2) {
                        fs.createFile(parts[1], parts[2]);
                    } else {
                        System.out.println("Usage: createFile [dirPath] [fileName]");
                    }
                    break;
                case "deleteFile":
                    if (parts.length > 2) {
                        fs.deleteFile(parts[1], parts[2]);
                    } else {
                        System.out.println("Usage: deleteFile [dirPath] [fileName]");
                    }
                    break;
                case "renameFile":
                    if (parts.length > 3) {
                        fs.renameFile(parts[1], parts[2], parts[3]);
                    } else {
                        System.out.println("Usage: renameFile [dirPath] [oldName] [newName]");
                    }
                    break;
                case "copyFile":
                    if (parts.length > 3) {
                        fs.copyFile(parts[1], parts[2], parts[3]);
                    } else {
                        System.out.println("Usage: copyFile [sourceDirPath] [destDirPath] [fileName]");
                    }
                    break;
                case "exit":
                    fs.close();
                    System.exit(0);
                    break;
                case "help":
                    fs.showHelp();
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }   
    }
}

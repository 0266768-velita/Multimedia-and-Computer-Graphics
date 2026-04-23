import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//File selection window
//Select multiple files at once
//Accept only:
//Images (.jpg, .png)
//Videos (.mp4)
public class Input {

    private final List<File> selectedFiles = new ArrayList<>();


    public int openFileChooser(JFrame parentFrame) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Add files  (Ctrl+A to select all, Ctrl+click for multiple)");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//It only allows files (not folders).
        if (!selectedFiles.isEmpty()) {
            chooser.setCurrentDirectory(selectedFiles.get(selectedFiles.size() - 1).getParentFile());
        } else {
            File picturesDir = new File(System.getProperty("user.home") + "/Pictures");
            if (picturesDir.exists()) chooser.setCurrentDirectory(picturesDir);
        }
//only this type of files
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Media files (jpg, png, mp4)",
                "jpg", "png", "mp4"
        );
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(parentFrame);
        int added  = 0;
//Only enter here if the user clicks "Accept".
        if (result == JFileChooser.APPROVE_OPTION) {
            for (File f : chooser.getSelectedFiles()) {
                if (!selectedFiles.contains(f)) {
                    selectedFiles.add(f);
                    added++;
                }
            }
        }

        return added;
    }

    //remove files
    public void removeFile(File file) {
        selectedFiles.remove(file);
    }

   //clear all files
    public void clearFiles() {
        selectedFiles.clear();
    }

   //getfiles
    public List<File> getSelectedFiles() {
        return selectedFiles;
    }
}
package ua.kpi.cad.workflows.common.interpreter;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Filesystem IO functions
 */
class FileInputOutput {
    private static String point = ".";
    private static String delimiter = "/";
    private static String space = " ";
    private static String argumentPrefix = "-";

    private static String programsFolder;
    private static String executionFolder;

    public static void setProgramsFolder(String folder) {
        programsFolder = folder;
    }

    public static void setExecutionFolder(String folder) {
        executionFolder = folder;
    }

    public static String getProgramsFolder() {
        return programsFolder;
    }

    public static String getExecutionFolder() {
        return executionFolder;
    }

    public static String getProgramExtension(String programName)
            throws WorkflowInterpretingException {
        if (doesProgramExist(programName, "m")) {
            return "m";
        } else if (doesProgramExist(programName, "sh")) {
            return "sh";
        } else {
            throw new WorkflowInterpretingException("Error! " + programName + ": no program found!", new Exception());
        }
    }

    public static String getProgramCallEssentials(String programName, String extension) {
        return point
                + delimiter
                + getProgramsFolder()
                + delimiter
                + repairFileName(programName)
                + point
                + extension;
    }

    public static String getFullArgument(String argumentName, String fileName, String executionSubfolderName) {
        return space
                + argumentPrefix
                + repairFileName(argumentName)
                + space
                + getExecutionFolder()
                + delimiter
                + executionSubfolderName
                + delimiter
                + repairFileName(fileName);
    }

    public static void createExecutionSubfolder(String executionSubfolderName) {
        (new File(executionFolder + delimiter + executionSubfolderName)).mkdirs();
    }

    public static String readFileFromProgramOutput(String programName, String executionSubfolderName) {
        String content = "";
        try {
            File inputFile = new File(executionFolder + delimiter + executionSubfolderName + delimiter
                    + repairFileName(programName));
            Scanner in = new Scanner(inputFile);
            while (in.hasNextLine()) {
                content += in.nextLine();
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeFileForProgramInput(String fileName, String executionSubfolderName, String content) {
        try {
            PrintWriter out = new PrintWriter(executionFolder + delimiter + executionSubfolderName + delimiter
                    + repairFileName(fileName));
            out.print(content);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String repairFileName(String oldFileName) {
        return oldFileName.replace(" ", "_").replace(",", "_");
    }

    private static boolean doesProgramExist(String programName, String extension) {
        File programFile = new File(getProgramCallEssentials(programName, extension));
        return programFile.exists();
    }

    public static boolean doesFileExist(String path) {
        File programFile = new File(executionFolder + delimiter + repairFileName(path));
        return programFile.exists();
    }
}

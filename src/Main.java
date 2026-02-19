import model.ImageModelImpl;
import view.ImageViewImpl;
import view.ImageViewGUI;
import controller.ImageControllerImpl;
import controller.ImageControllerGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The Main class is the entry point of the application. It handles launching the
 * application in different modes: graphical user interface (GUI), interactive
 * text mode, and script execution mode.
 */
public class Main {

  /**
   * Main method that launches the application in the appropriate mode based on
   * the command-line arguments provided.
   *
   * @param args Command-line arguments to determine the mode of execution.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      launchGUI();
    } else if (args.length == 2 && args[0].equalsIgnoreCase("-file")) {
      executeScriptMode(args[1]);
    } else if (args.length == 1 && args[0].equalsIgnoreCase("-text")) {
      executeInteractiveMode();
    } else {
      printUsageInstructions();
    }
  }

  /**
   * Launches the graphical user interface (GUI) mode of the application.
   */
  private static void launchGUI() {
    javax.swing.SwingUtilities.invokeLater(() -> {
      ImageModelImpl model = new ImageModelImpl();
      ImageViewGUI view = new ImageViewGUI();
      new ImageControllerGUI(model, view);
      view.setVisible(true);
    });
  }

  /**
   * Executes commands from a script file.
   *
   * @param filePath Path to the script file to be executed.
   */
  private static void executeScriptMode(String filePath) {
    File scriptFile = new File(filePath);
    if (!scriptFile.exists()) {
      System.err.println("Error: Script file not found at " + filePath);
      return;
    }

    try (Scanner scanner = new Scanner(scriptFile)) {
      ImageModelImpl model = new ImageModelImpl();
      ImageViewImpl view = new ImageViewImpl();
      ImageControllerImpl controller = new ImageControllerImpl(model, view);

      while (scanner.hasNextLine()) {
        String command = scanner.nextLine().trim();
        if (!command.isEmpty() && !command.startsWith("#")) {
          controller.processCommand(command);
        }
      }

      System.out.println("Script execution completed successfully.");
    } catch (FileNotFoundException e) {
      System.err.println("Error: Unable to read the script file.");
    } catch (Exception e) {
      System.err.println("Error: An unexpected error occurred while executing the script: "
              + e.getMessage());
    }
  }

  /**
   * Starts the interactive text mode of the application, allowing users to
   * enter commands through the console.
   */
  private static void executeInteractiveMode() {
    System.out.println("Interactive mode started. Enter commands below:");
    System.out.println("Type 'exit' to quit.");

    Scanner scanner = new Scanner(System.in);
    ImageModelImpl model = new ImageModelImpl();
    ImageViewImpl view = new ImageViewImpl();
    ImageControllerImpl controller = new ImageControllerImpl(model, view);

    while (true) {
      System.out.print("> ");
      String command = scanner.nextLine().trim();
      if (command.equalsIgnoreCase("exit")) {
        System.out.println("Exiting interactive mode.");
        break;
      }

      try {
        controller.processCommand(command);
      } catch (IllegalArgumentException e) {
        System.err.println("Error: " + e.getMessage());
      }
    }
  }

  /**
   * Prints the usage instructions for running the application.
   */
  private static void printUsageInstructions() {
    System.err.println("Invalid arguments. Usage:");
    System.err.println("  java -jar Assignment_6.jar               : " +
            "Launch the graphical user interface (GUI)");
    System.err.println("  java -jar Assignment_6.jar -file <path>  : " +
            "Execute script from file and shut down");
    System.err.println("  java -jar Assignment_6.jar -text  : Open interactive text mode");
  }
}

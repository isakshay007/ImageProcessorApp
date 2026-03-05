package controller.command;

import java.util.StringTokenizer;

/**
 * Command interface for the Command pattern.
 * Each implementation encapsulates a specific image processing command.
 */
public interface ImageCommand {

  /**
   * Executes the command using the remaining tokens from the parsed command string.
   *
   * @param tokenizer the {@link StringTokenizer} containing the command parameters.
   * @throws Exception if an error occurs during command execution.
   */
  void execute(StringTokenizer tokenizer) throws Exception;
}

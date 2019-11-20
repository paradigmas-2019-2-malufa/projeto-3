package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ComunicadorAPI {

    private static final ProcessBuilder pb = new ProcessBuilder();
    private static String command = "echo  'Olá, mundo !'";
    private static String prompt = "bash";
    private static String op = "-c";

    public static void enviarDados() {
        pb.command(prompt, op, command);
        processoSO(pb);

    }

    public static void enviarDados(String command) {
        pb.command(prompt, op, command);
        processoSO(pb);
    }

    public static void enviarDados(String prompt, String op, String command) {
        pb.command(prompt, op, command);
        processoSO(pb);
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setOp(String op) {
        this.op = op;
    }

    private static void processoSO(ProcessBuilder pb) {
        try {
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error : " + exitCode);
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }

}

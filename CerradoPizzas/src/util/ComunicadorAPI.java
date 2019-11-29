package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ComunicadorAPI {

    private static final ProcessBuilder pb = new ProcessBuilder();
    private static String command = "echo  'Olá, mundo !'";
    private static String prompt = "bash";
    private static String op = "-c";
    public static String SERVIDOR = " http://localhost:3000/pedidos";
    public static String COMUNICADOR = "curl --header \"Content-Type: application/json\" "
            + "  --request POST "
            + "  --data '";

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

    public static void setCommand(String c) {
        command = c;
    }

    public static  void setPrompt(String p) {
        prompt = p;
    }

    public static void setOp(String o) {
        op = o;
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

    public static void enviarAPI(String json) {
        final String url = "http://localhost:3000/post/";
      
         StringBuilder mensagem = new StringBuilder(COMUNICADOR) ;
               
         mensagem.append( (json + "' "));
         mensagem.append(SERVIDOR);
         System.out.println(mensagem.toString());
         enviarDados(mensagem.toString());
         
    }

}

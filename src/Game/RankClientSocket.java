package Game;

import java.io.*;
import java.net.*;


public class RankClientSocket {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("클라이언트로 실행하려면 'client'를 입력하세요:");
        try {
            String mode = reader.readLine().trim().toLowerCase();
            if (mode.equals("client")) {
                System.out.println("서버 주소를 입력하세요 (localhost 또는 IP 주소):");
                String serverAddress = reader.readLine().trim();
                startClient(serverAddress, 8000); // 클라이언트 실행
            } else {
                System.out.println("잘못된 입력입니다. 프로그램을 종료합니다.");
            }
        } catch (IOException e) {
            System.err.println("오류 발생: " + e.getMessage());
        }
    }

    // 클라이언트 실행 메서드
    public static void startClient(String serverAddress, int port) {
        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("확인할 게임 난이도를 입력하세요 (예: easy, normal, hard):");
            String game = consoleReader.readLine().trim();  // 게임 난이도 입력 받기
            out.println(game);  // 서버로 난이도 전송

            // 서버로부터 응답 출력
            System.out.println("서버 응답:");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("클라이언트 오류: " + e.getMessage());
        }
    }
}

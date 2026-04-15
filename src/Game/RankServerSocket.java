package Game;

import java.io.*;
import java.net.*;
import java.util.List;

public class RankServerSocket {
    public static void main(String[] args) {
        int port = 8000; // 서버 포트

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("랭크 서버가 실행 중입니다. 포트: " + port);

            while (true) {
                // 클라이언트 연결 대기
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트가 연결되었습니다: " + clientSocket.getInetAddress());

                // 클라이언트 요청 처리
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String game = in.readLine(); // 클라이언트에서 난이도 요청
                    System.out.println("요청 받은 게임 난이도: " + game);

                    // 랭킹 데이터 로드
                    List<rank.Rank> ranking = rank.loadRanking(game);
                    StringBuilder response = new StringBuilder("=== " + game + " 랭킹 ===\n");

                    for (rank.Rank r : ranking) {
                        response.append(r.toString()).append("\n");
                    }

                    // 클라이언트에 응답
                    out.println(response.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("서버 오류: " + e.getMessage());
        }
    }
}

package Game;

import java.io.*;
import java.util.*;

public class rank {
    // 사용자 데이터를 파일에 저장
    public static void saveUserData(String game, String name, String time) {
        // 지정한 절대 경로를 사용
        String filePath = getFilePath(game);

        // 디렉터리 생성
        File file = new File(filePath);
        File directory = file.getParentFile(); // 디렉터리 경로
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉터리가 없으면 생성
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(name + "," + time); // 이름과 시간을 저장
            writer.newLine(); //줄바꿈
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    // 사용자 데이터를 파일에서 읽기
    public static Map<String, String> loadUserData(String game) {
        Map<String, String> userData = new HashMap<>();
        String filePath = getFilePath(game);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // "이름,시간" 형식으로 저장되므로 ','로 분리
                if (data.length == 2) {
                    userData.put(data[0], data[1]); // 이름과 시간을 배열로 저장
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No data found for " + game + ". File will be created on next save.");
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
        return userData;
    }

    // 랭크 클래스를 정의
    static class Rank implements Comparable<Rank> {
        String time; // 시간 필드로 변경
        String name;

        Rank(String name, String time) {
            this.name = name;
            this.time = time; // 시간 저장
        }

        @Override
        public int compareTo(Rank r) {
            // 시간 비교를 위해 파싱 후 비교할 수 있도록 구현
            String[] thisTimeParts = this.time.split("분 ");
            String[] otherTimeParts = r.time.split("분 ");
            int thisMinutes = Integer.parseInt(thisTimeParts[0]);
            int thisSeconds = Integer.parseInt(thisTimeParts[1].replace("초", "").trim());
            int otherMinutes = Integer.parseInt(otherTimeParts[0]);
            int otherSeconds = Integer.parseInt(otherTimeParts[1].replace("초", "").trim());

            // 총 초로 변환하여 비교
            int thisTotalSeconds = thisMinutes * 60 + thisSeconds;
            int otherTotalSeconds = otherMinutes * 60 + otherSeconds;

            return thisTotalSeconds - otherTotalSeconds; // 점수가 낮은 순서로 정렬
        }

        @Override
        public String toString() {
            return name + " : " + time; // 이름과 시간을 문자열로 반환
        }
    }

    public static List<Rank> loadRanking(String game) {
        Map<String, String> userData = loadUserData(game);
        List<Rank> ranking = new ArrayList<>();

        userData.forEach((String name, String time) -> {
            ranking.add(new Rank(name, time)); // 사용자 데이터를 랭크 리스트에 추가
        });
        Collections.sort(ranking); // 시간 기준으로 정렬
        return ranking; // 정렬된 랭킹 리스트 반환
    }

    // 게임 난이도에 따른 파일 경로 반환
    private static String getFilePath(String game) {
        return "./src/data/" + game + "_data.txt";
    }
}

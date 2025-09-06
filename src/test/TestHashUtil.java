package test;

import backend.util.HashUtil;

public class TestHashUtil {
    public static void main(String[] args) {
        String password = "mySecret123";
        String hashed = HashUtil.hashPassword(password);

        System.out.println("Raw: " + password);
        System.out.println("Hashed: " + hashed);

        boolean check = HashUtil.verifyPassword("mySecret123", hashed);
        System.out.println("Password match: " + check);

        boolean wrongCheck = HashUtil.verifyPassword("wrongPass", hashed);
        System.out.println("Password match with wrong input: " + wrongCheck);
    }
}

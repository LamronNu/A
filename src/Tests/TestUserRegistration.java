package Tests;


import WebService.Domain.General.AuctionPublisher;

public class TestUserRegistration {
    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        test4();
    }

//    public static void test1() {
//        UserCard userCard = new UserCard("login1", "123","name1","lastname1");
//        userCard.RegisterData();
//    }
//
//    public static void test2() {
//        for (int i = 0; i < 10; i++) {
//            UserCard userCard = new UserCard("login" + i, "123","test_name","test_lastname");
//            userCard.RegisterData();
//        }
//    }

//    public static void test3() {
//        //publish ws
//        new AuctionPublisher().publish();
//        //show form
//        RegistrationForm test001 = new RegistrationForm();
//        //test001.setVisible(true);
//        //test001.show();
//    }

    public static void test4() {
        //publish ws
        new AuctionPublisher().publish();
        //show form
       // BasicForm test001 = new AuthenticationForm();
        //test001.setVisible(true);
        //test001.show();
    }
}


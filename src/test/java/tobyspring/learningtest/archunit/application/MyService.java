package tobyspring.learningtest.archunit.application;

import tobyspring.learningtest.archunit.adapter.MyAdapter;

public class MyService {
    MyService2 myService2;

    void run() {
        myService2 = new MyService2();
        System.out.println(myService2);
    }
}

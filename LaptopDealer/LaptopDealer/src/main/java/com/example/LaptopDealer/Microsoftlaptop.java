package com.example.LaptopDealer;


public class Microsoftlaptop implements Brand {
    Processors processor;
    public Microsoftlaptop(Processors processor) {
        this.processor = processor;
    }
    @Override
    public void ShowDetails() {
        System.out.println("You have selected Microsoftlaptop"+processor.showProcessorsDetails());
    }
}

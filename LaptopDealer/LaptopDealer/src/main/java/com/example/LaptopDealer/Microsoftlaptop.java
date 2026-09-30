package com.example.LaptopDealer;


public class Microsoftlaptop implements Brand {
    Processors processors;
//    public Microsoftlaptop(Processors processors) {
//        this.processors = processors;
//    }

    public void setProcessors(Processors processors) {
        this.processors = processors;
    }

    @Override
    public void ShowDetails() {
        System.out.println("You have selected Microsoftlaptop"+processors.showProcessorsDetails());
    }
}

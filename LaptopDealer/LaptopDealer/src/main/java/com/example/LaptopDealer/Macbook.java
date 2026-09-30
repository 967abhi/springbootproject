package com.example.LaptopDealer;

public class Macbook implements Brand {
    Processors processors;

    public void setProcessors(Processors processors) {
        this.processors = processors;
    }

    //    public Macbook(Processors processors) {
//        this.processors = processors;
//    }
    @Override
    public void ShowDetails() {
        System.out.println("You have selected Macbook"+processors.showProcessorsDetails());
    }
}

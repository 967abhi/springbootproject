package com.example.LaptopDealer;


public class Dell implements  Brand{
     Processors processors;

    public void setProcessors(Processors processors) {
        this.processors = processors;
    }

    //     public Dell(Processors processors){
//        this.processors = processors;
//     }
    @Override
    public void ShowDetails(){
        System.out.println("You have selected Dell Laptop"+processors.showProcessorsDetails());
    }

}

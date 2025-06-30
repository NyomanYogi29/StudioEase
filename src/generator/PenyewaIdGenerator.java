/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package generator;

/**
 *
 * @author nyoma
 */
public class PenyewaIdGenerator {
    private String prefix;
    private int counter;
    private String format;
    
    public PenyewaIdGenerator(String prefix, int counter, int numberOfDigits)
    {
        this.prefix = prefix;
        this.counter = counter;
        this.format = "%s%0" + numberOfDigits + "d";
    }
    
    public String generateNextId()
    {
        this.counter++;
        return String.format(this.format, this.prefix, this.counter);
    }
}

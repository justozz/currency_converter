package com.justozz.currencyconverter.Data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

/**
 * Created by justs on 17.04.2017.
 */

@Root(name = "Valute")
public class CurrencyEntry {
    @Attribute(name = "ID")
    protected String id;

    @Element(name = "NumCode")
    protected int numCode;

    @Element(name = "CharCode")
    protected String charCode;

    @Element(name = "Nominal")
    protected int nominal;

    @Element(name = "Name")
    protected String name;

    @Element(name = "Value")
    private String valueStr;

    protected double value;

    @Validate
    private void validate() {
        value = Double.parseDouble(valueStr.replace(',', '.'));
    }

    public String getId() {
        return id;
    }

    public int getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
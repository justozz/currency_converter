package com.justozz.currencyconverter.Data;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by justs on 17.04.2017.
 */

@Root
public class CurrencyList {
    @Attribute(name = "Date")
    protected String dateStr;

    @Attribute(name = "name")
    protected String name;

    @ElementList(inline = true)
    protected List<CurrencyEntry> currencyList;
}

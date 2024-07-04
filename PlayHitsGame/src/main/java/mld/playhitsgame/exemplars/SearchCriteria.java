/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

/**
 *
 * @author miguel
 */
public class SearchCriteria {
    
    private String attr;
    private Object value;
    private SearchOperation searchOperation;
    public SearchCriteria() {}
    public SearchCriteria(String attr, Object value, SearchOperation searchOperation) {
        this.attr = attr;
        this.value = value;
        this.searchOperation = searchOperation;
    }
    public String getAttr() {
        return attr;
    }
    public void setAttr(String attr) {
        this.attr = attr;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public SearchOperation getSearchOperation() {
        return searchOperation;
    }
    public void setSearchOperation(SearchOperation searchOperation) {
        this.searchOperation = searchOperation;
    }
    
}

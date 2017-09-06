package net.edmondschools.accounts.beans;

public class SearchResult {
    private String first;
    private String last;

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }
    
    public SearchResult getObj() {
        return this;
    }
    
    public boolean isEmpty() {
        return first == null && last == null;
    }
    
    public void process(String s) {
        if (s != null && !s.equals("")) {
            first = null;
            last = null;

            String arr[] = s.trim().split(",");
            
            if (!arr[0].equals("")) {
                last = arr[0];
            }
            
            if (arr.length > 1 && !arr[1].equals("")) {
                first = arr[1];
            }
        }
    }
}
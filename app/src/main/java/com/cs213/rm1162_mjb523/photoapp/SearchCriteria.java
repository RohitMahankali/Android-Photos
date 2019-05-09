package com.cs213.rm1162_mjb523.photoapp;

import java.util.ArrayList;

public class SearchCriteria{
    private boolean and;
    private boolean person;
    private String val;
    private Tag tag;
    private static String[] args = {"AND ", "OR ", "P: ", "L: "};

    public SearchCriteria(boolean AND, boolean PERSON, String VAL){
        this.and = AND;
        this.person = PERSON;
        this.val = VAL;
        this.tag = new Tag((PERSON)?"person":"location", VAL);
    }

    /**
     * @return if AND is true
     */
    public boolean isAnd() {
        return this.and;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String str = "";
        if(and)
            str += args[0];
        else
            str += args[1];
        if(person)
            str += args[2];
        else
            str += args[3];
        str += val;
        return str;
    }

    public boolean isPerson() {
        return person;
    }

    public boolean meetsCriteria(Photo p) {
        boolean retval = false;
        int i = 0;
        ArrayList<Tag> tags = p.getTags();
        while(!retval && i < tags.size()) {
            if(this.tag != null) {
                retval = this.tag.sameTag(tags.get(i));
            }
            i++;
        }
        return retval;
    }
}

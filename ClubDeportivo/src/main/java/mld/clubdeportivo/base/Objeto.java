/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base;

import java.io.Serializable;
import static java.lang.String.valueOf;

/**
 *
 * @author Miguel
 */
public abstract class Objeto extends Object
        implements Serializable {
    
    public static final long UNSAVED_VALUE = -1;
    private long id = UNSAVED_VALUE;

    public long getId() {
        return id;
    }
    
    public String getIdTxt() {
        return valueOf(this.getId());
    }

    public void setId(long id) {

        if (this.getId() != UNSAVED_VALUE)
            throw new UnsupportedOperationException("Operacion no posible");

        this.id = id;
    }

    public boolean equals(Objeto obj) {

        return obj.getClass() == this.getClass() &&
                obj.getId() == this.getId();
    }

}

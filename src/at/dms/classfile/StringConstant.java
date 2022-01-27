/*
 * Copyright (C) 1990-99 DMS Decision Management Systems Ges.m.b.H.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package at.dms.classfile;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Wrap an String constant reference with this CPE.
 */
public class StringConstant
    extends PooledConstant
{
    /**
     * String value of constant.
     */
    private AsciiConstant value;

    /**
     * Constructs a new String (UNICODE) pooled constant.
     *
     * @param value Value for String constant
     */
    public StringConstant(String value)
    {
        this.value = new AsciiConstant(value);
    }

    /**
     * Constructs a new String (UNICODE) pooled constant.
     *
     * @param value
     * Value for String constant.
     */
    public StringConstant(AsciiConstant value)
    {
        this.value = value;
    }

    /**
     * Returns the associated literal
     */
    Object getLiteral()
    {
        return value.getValue();
    }

    /**
     * hashCode (a fast comparison)
     * CONVENTION: return XXXXXXXXXXXX << 4 + Y
     * with Y = ident of the type of the pooled constant
     */
    public final int hashCode()
    {
        return (value.hashCode() & 0xFFFFFFF0) + POO_STRING_CONSTANT;
    }

    /**
     * equals (an exact comparison)
     * ASSERT: this.hashCode == o.hashCode ===> cast
     */
    public final boolean equals(Object o)
    {
        return (o instanceof StringConstant) &&
            ((StringConstant)o).value.equals(value);
    }

    /**
     * Check location of constant value on constant pool.
     *
     * @param pc the already in pooled constant.
     * ASSERT pc.getClass() == this.getClass()
     */
    final void resolveConstants(PooledConstant pc)
    {
        setIndex(pc.getIndex());
        value.setIndex(((StringConstant)pc).value.getIndex());
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp The constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(value);
    }

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool.
     *
     * @param cp      the constant pool that contain all data
     * @param out     the file where to write this object info
     *
     * @exception IOException
     * An io problem has occured.
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException
    {
        out.writeByte(CST_STRING);
        out.writeShort(value.getIndex());
    }
}
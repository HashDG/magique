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

import at.dms.util.InternalError;

/**
 * A ClassConstant record from the constant pool.
 */
public class ClassConstant
    extends PooledConstant
{
    /**
     * The fully qualified name of the class that is referred to by
     * this ClassConstant.
     */
    private AsciiConstant name;

    /**
     * Constructs a new class constant.
     *
     * @param name
     * The qualified name of the class.
     */
    public ClassConstant(String name)
    {
        this(new AsciiConstant(name));
    }

    /**
     * Constructs a new class constant.
     *
     * @param name
     * The qualified name of the class.
     */
    public ClassConstant(AsciiConstant name)
    {
        this.name = name;
    }

    /**
     * Returns the fully qualified name of the class that is referred to by
     * this ClassConstant.
     */
    public String getName()
    {
        return name.getValue();
    }

    /**
     * Returns the associated literal: this constant type has none.
     */
    Object getLiteral()
    {
        throw new InternalError("attempt to read literal in a class constant");
    }

    /**
     * hashCode (a fast comparison)
     * CONVENTION: return XXXXXXXXXXXX << 4 + Y
     * with Y = ident of the type of the pooled constant
     */
    public final int hashCode()
    {
        // we know already that name is an ascii: &
        return (name.hashCode() & 0xFFFFFFF0) + POO_CLASS_CONSTANT;
    }

    /**
     * equals (an exact comparison)
     * ASSERT: this.hashCode == o.hashCode ===> cast
     */
    public final boolean equals(Object o)
    {
        return (o instanceof ClassConstant) &&
            ((ClassConstant)o).name.equals(name);
    }

    /**
     * Check location of constant value on constant pool.
     *
     * @param pc
     * The constant that is already in the constant pool.
     * ASSERT pc.getClass() == this.getClass()
     */
    final void resolveConstants(PooledConstant pc)
    {
        setIndex(pc.getIndex());
        name.setIndex(((ClassConstant)pc).name.getIndex());
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp
     * The constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(name);
    }

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool.
     *
     * @param cp
     * The constant pool that contain all data.
     * @param out
     * The file where to write this object info.
     *
     * @exception IOException
     * An io problem has occured.
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException
    {
        out.writeByte(CST_CLASS);
        out.writeShort(name.getIndex());
    }
}
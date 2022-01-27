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
 * Some instructions are perniticky enough that its simpler
 * to write them separately instead of smushing them with
 * all the rest. the multiarray instruction is one of them.
 */
public class MultiarrayInstruction
    extends Instruction
{
    /**
     * Base type of array.
     */
    private ClassConstant type;

    /**
     * Number of dimensions.
     */
    private int dims;

    /**
     * Constructs a new multiarray instruction.
     *
     * @param name
     * The qualified name of the base class.
     * @param dims
     * Number of dimensions for the array.
     */
    public MultiarrayInstruction(String name, int dims)
    {
        super(opc_multianewarray);

        this.type = new ClassConstant(name);
        this.dims = dims;
    }

    /**
     * Constructs a new multiarray instruction from a class file.
     *
     * @param type
     * The base class as a pooled constant.
     *
     * @param dims
     * Number of dimensions for the array.
     */
    public MultiarrayInstruction(ClassConstant type, int dims)
    {
        super(opc_multianewarray);

        this.type = type;
        this.dims = dims;
    }

    /**
     * Returns true iff control flow can reach the next instruction
     * in textual order.
     */
    public boolean canComplete()
    {
        return true;
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp the constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(type);
    }

    /**
     * Returns the number of bytes used by the the instruction in the code
     * array.
     */
    int getSize()
    {
        return 1 + 3;
    }

    /**
     * Return the type of the array.
     */
    public String getType()
    {
        return type.getName();
    }

    /**
     * Return the number of dimension of this array.
     */
    public int getDimension()
    {
        return dims;
    }

    /**
     * Returns the size of data pushed on the stack by this instruction.
     */
    public int getPushedOnStack()
    {
        return 1;
    }

    /**
     * Return the amount of stack (positive or negative) used by this
     * instruction.
     */
    public int getStack()
    {
        return 1 - dims;
    }

    /**
     * Returns the type pushed on the stack.
     */
    public byte getReturnType()
    {
        return TYP_REFERENCE;
    }

    /**
     * Write this instruction into a file.
     *
     * @param cp
     * The constant pool that contain all data.
     *
     * @param out
     * The file where to write this object info.
     *
     * @exception IOException
     * An io problem has occured.
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException
    {
        out.writeByte((byte)getOpcode());

        out.writeShort(type.getIndex());
        out.writeByte((byte)(dims & 0xFF));
    }
}
package barbearia;

/**
* barbearia/BarbeiroHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from barbearia.idl
* Terça-feira, 9 de Junho de 2009 09h12min53s BRT
*/

public final class BarbeiroHolder implements org.omg.CORBA.portable.Streamable
{
  public barbearia.Barbeiro value = null;

  public BarbeiroHolder ()
  {
  }

  public BarbeiroHolder (barbearia.Barbeiro initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = barbearia.BarbeiroHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    barbearia.BarbeiroHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return barbearia.BarbeiroHelper.type ();
  }

}
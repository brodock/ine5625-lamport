package barbearia;

/**
* barbearia/MensagemHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from barbearia.idl
* Terça-feira, 9 de Junho de 2009 09h12min53s BRT
*/

public final class MensagemHolder implements org.omg.CORBA.portable.Streamable
{
  public barbearia.Mensagem value = null;

  public MensagemHolder ()
  {
  }

  public MensagemHolder (barbearia.Mensagem initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = barbearia.MensagemHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    barbearia.MensagemHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return barbearia.MensagemHelper.type ();
  }

}

package org.jboss.forge.shell.plugins.builtin;

import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceFlag;
import org.jboss.forge.shell.plugins.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mike Brock
 */
@Alias("fingerprint")
@Topic("File & Resources")
@Help("calculate a hash for the specified resource")
public class FingerprintPlugin implements Plugin
{
   @DefaultCommand
   public void run(
            @PipeIn final InputStream pipeIn,
            @Option(name = "cipher", help = "hash cipher to use (default: 'SHA-256')",
                     defaultValue = "SHA-256")
            String cipher,
            @Option(description = "FILE ...", defaultValue = "*") Resource<?>[] resources,
            final PipeOut pipeOut
   ) throws IOException
   {

      cipher = cipher.toUpperCase().trim();
      String name = null;

      try
      {
         final MessageDigest md = MessageDigest.getInstance(cipher);

         if (pipeIn != null)
         {
            name = "<pipe>";
            fingerprint(pipeIn, md);
         }
         else if (resources != null)
         {
            InputStream inputStream = null;
            StringBuilder names = new StringBuilder();
            for (Resource<?> r : resources)
            {
               if (r.isFlagSet(ResourceFlag.Node)) continue;
               
               names.append(r.getName()).append(" ");
               try
               {
                  fingerprint(inputStream = r.getResourceInputStream(), md);
               }
               finally
               {
                  if (inputStream != null) inputStream.close();
               }
            }
            name = names.toString().trim();
         }

         if (pipeOut.isPiped()) {
            name = "";
         }

         pipeOut.print(name);
         pipeOut.print(" ");

         for (byte b : md.digest())
         {
            pipeOut.print(Integer.toHexString(0xFF & b));
         }
         pipeOut.println();
      }
      catch (NoSuchAlgorithmException e)
      {
         throw new RuntimeException("the hashing algorithm '" + cipher + "' could not be found");
      }
   }

   private void fingerprint(InputStream instream, MessageDigest md) throws IOException
   {
      byte b;
      while ((b = (byte) instream.read()) != -1)
      {
         md.update(b);
      }
   }
}

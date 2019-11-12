/***********************************************************************************************************
** Nombre :   Joaquin Montero. 2DAM. DI --> T2A4                                                        ****
** Ejercicio : AppAgenda ( Agenda con DB apache derbie                                                  ****
** Fuente : https://javiergarciaescobedo.es/programacion-en-java/9-bases-de-datos                       ****
**          pdf ( Desde página 26 hasta final de PDF )                                                  ****
************************************************************************************************************/
package appagenda;

import entidades.Provincia;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author joaquin
 */
public class AppAgenda 
{

    public static void main(String[] args)
    {
        //Conecta con la base de datos
        Map<String, String> emfProperties = new HashMap<>();
        emfProperties.put("javax.persistence.jdbc.schema-generation.database.action", "create");
        emfProperties.put("javax.persistence.jdbc.url","jdbc:derby:BDAgenda;create=true");
         // Instancia objeto EntityManagerFactory
        // Pasa por parámetro el nombre de la unidad de persistencia que contiene los datos de conexión
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em = emf.createEntityManager();
        
        // Inicia transaccion con base de datos para poder hacer modificaciones
        // inserciones, modificaciones y borrado.
        em.getTransaction().begin();
        
        // Confirma el volcado definitivo a la base de datos
        em.getTransaction().commit();

//=========================================================================================
 /*        
        Aniade Sevilla a tabla Provincia
        Provincia provinciaSevilla=new Provincia();
        provinciaSevilla.setNombre("Sevilla");
        
        
        // Para que ambos objetos se almacenen en la base de datos se deberá iniciar la transacción,
        // utilizar el método persist sobre los objetos, y por último volcar los cambios a la base de datos:
        em.getTransaction().begin();
        em.persist(provinciaSevilla);
        em.getTransaction().commit();
 */       
        
//=============================================================================================
/* 
       //Borrar una columna de provincia por ejemplo        
        Provincia provinciaId15 = em.find(Provincia.class, 110);
        em.getTransaction().begin();
        if (provinciaId15 != null)
        {
         em.remove(provinciaId15);
        }
        else
        {
            System.out.println("No hay ninguna provincia con ID=110");
            
        }
        em.getTransaction().commit();
*/        
 
//==========================================================================================       
        
/*
        
        //    como tenemos creada esta consulta en clase Provincia
        //        @NamedQuery(name="Provincia.findByNombre",query="SELECT p FROM Provincia p WHERE p.nombre = :nombre")})
        
        //    Podemos consultar por nombre : 
        
                Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
                queryProvinciaCadiz.setParameter("nombre", "Cádiz");
        
                List<Provincia> listProvinciasCadiz =queryProvinciaCadiz.getResultList();
                for(Provincia provinciaCadiz:listProvinciasCadiz)
                {
                    System.out.println(provincia.getId()+":");
                    System.out.println(provincia.getNombre());
                }
*/
 
//=========================================================================================================
/*       
        // obtener los objetos Provincia cuyo nombre sea "Cádiz", mostrando en la salida
        //  estándar el ID del registro y el nombre de la Provincia:
            Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
            queryProvinciaCadiz.setParameter("nombre", "Cádiz");
            List<Provincia> listProvinciasCadiz =queryProvinciaCadiz.getResultList();
            
            for(Provincia provinciaCadiz:listProvinciasCadiz)
            {
                System.out.println(provincia.getId()+":");
                System.out.println(provincia.getNombre());
            }
        
*/       
//===========================================================================================================  
        
/*
        // obtener el objeto Provincia cuyo ID sea 2:
        Provincia provinciaId2=em.find(Provincia.class,2);
        if (provinciaId2 != null)
        {
            System.out.print(provinciaId2.getId() + ":");
            System.out.println(provinciaId2.getNombre());
        } 
        else 
        {
            System.out.println("No hay ninguna provincia con ID=2");
        }
*/
        
//==============================================================================================================

/*
        // modificación de objetos el método merge de EntityManager indicando como parámetro el objeto que se desea
        // actualizar en la base de datos
        Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
        queryProvinciaCadiz.setParameter("nombre", "Cádiz");
        List<Provincia> listProvinciasCadiz =queryProvinciaCadiz.getResultList();
        em.getTransaction().begin();
        
        for(Provincia provinciaCadiz : listProvinciasCadiz)
        {
            provinciaCadiz.setCodigo("CA");
            em.merge(provinciaCadiz);
        }
        em.getTransaction().commit();
*/       
 
//==================================================================================================================
        
        // Cierra conexion una vez realizadas todas las operaciones
        em.close();
        emf.close();
        try
        {
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        }
        catch(SQLException ex) {}    
    } 
}

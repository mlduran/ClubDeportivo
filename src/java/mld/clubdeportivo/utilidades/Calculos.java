package mld.clubdeportivo.utilidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import mld.clubdeportivo.base.Equipo;

public class Calculos {

    	
    private Calculos() {
    }
    
    static private int getNum(int rangoInicial, int rangoFinal){
        
        Random rnd = new Random();
        int num = (int) (rnd.nextDouble() * (rangoFinal - rangoInicial) + rangoInicial);
        
        return num;
        
    }
    
    static private int getNum(int rangoFinal){
        
        return getNum(0, rangoFinal);
        
    }
    
		
    static public int[] valoresAleatorios(int n_nums, int media,
            int rango_min, int rango_max)
    {
        // Devuelve un vector de n_nums numeros que suman la
        // media definida en med_val

        int[] lista_val = new int[n_nums];
        int suma_total = media * n_nums;
        boolean ok = false;
        int x;

        while (ok == false)
        {
            int suma = 0;

            for (int i = 1; i < n_nums; i++)
            {
                x = getNum(rango_min, rango_max);
                suma = suma + x;
                lista_val[i - 1] = x;
            }

            if ((suma < (suma_total - rango_min)) && (suma > (suma_total - rango_max)))
            {
                x = suma_total - suma;
                lista_val[n_nums - 1] = x;
                ok = true;
            }
        }

        return lista_val;

    }
    
    static public ArrayList<Integer> listaValoresAleatorios(int n_nums, int media,
            int rango_min, int rango_max)
    {
        // Devuelve un vector de n_nums numeros que suman la
        // media definida en med_val

        int[] vals = valoresAleatorios(n_nums, media,rango_min, rango_max);
        ArrayList<Integer> listaVals = new ArrayList<Integer>();
        
        for (int i = 0; i < vals.length; i++) 
            listaVals.add(vals[i]);

        return listaVals;

    }
	
    static public int valorAleatorio(int rango_min, int rango_max)
    {
        // Devuelve un numero aleatorio entre los rangos
        // ejemplo min = 1 max = 10 da un numero de 1 a 9

        int x;

        x = getNum(rango_min, rango_max);

        return x;

    }
    
    static public int valorAleatorio(int rango_max)
    {
        // Devuelve un numero aleatorio entre los rangos
        // ejemplo min = 0 max = 9 da un numero de 0 a 8

        int x;

        x = getNum(rango_max);

        return x;

    }

    static public int[] valoresAleatoriosSinRepetir(int n, int rango_min, int rango_max)
    {
        // Devuelve vector de n numeros aleatorios entre los rangos

        if (rango_max - rango_min < n)
            throw new IllegalArgumentException("n no puede ser mayor que la diferencia de rangos");

        int x[] = new int[n];

        for (int i = 0; i < n; i++){
            int nx = 0;
            boolean repetido = true;
            while(repetido){
                repetido = false;
                nx = getNum(rango_min, rango_max);
                for (int ii = 0; ii < i; ii++){
                    if (nx == x[ii]){
                        repetido = true;
                        break;
                    }
                }
            }
            x[i] = nx;
        }

        return x;

    }
    
    static public int[] valoresAleatorios(int n, int rango_min, int rango_max)
    {
        // Devuelve vector de n numeros aleatorios entre los rangos

        if (rango_max - rango_min < n)
            throw new IllegalArgumentException("n no puede ser mayor que la diferencia de rangos");

        int x[] = new int[n];

        for (int i = 0; i < n; i++){
            int nx = getNum(rango_min, rango_max);
            x[i] = nx;
        }

        return x;

    }

    static public boolean obtenerResultado(int valor1, int valor2)
    {// Devuelve true si aleatoriamente el valor1 supera al valor2
        boolean result;
        int v1 = 0, v2 = 0;

        for (int i = 0; i < 10; i++){
            v1 = getNum(valor1);
            v2 = getNum(valor2);
            if (v1 != v2) break;
        }

        // en caso de persistir empate gana valor1
        result = v1 > v2;

        return result;
    }

    static public boolean obtener(int valor)
    {// Devuelve true si aleatoriamente el resultado es 0 entre un rango de 0 a valor
        // valor 2 hace un 50% entre true y false
        int v;

        v = getNum(valor);

        return v == 0;
    }


    
    static public ArrayList<JornadaTmp> crearCalendarioLiga(ArrayList<Equipo> eqs){
        // Recibe un array con los ids de los equipos y devuelve
        // un array de arrays con jornada, eqLocal, eqVisitante
        //

        // este metodo hace esta rotacion
        // 1ª ronda: (1 contra 14, 2 contra 13, … )
        // 1  2  3  4  5  6  7
        // 14 13 12 11 10 9  8
        // 2ª ronda: (1 contra 13, 14 contra 12, … )
        // 1  14 2  3  4  5  6
        // 13 12 11 10 9  8  7
        // 3ª ronda: (1 contra 12, 13 contra 11, … )
        // 1  13 14 2  3  4  5
        // 12 11 10 9  8  7  6

        // Para trabajar mas facilmente nos quedamos con los ids


        ArrayList calendario = new ArrayList();

        Collections.shuffle(eqs);

        int n = eqs.size();

        Equipo[] g1 = new Equipo[(n/2)+1], g2 = new Equipo[(n/2)+1];

        // Este mapeo es para decidir que equipo juega
        // en casa
        HashMap<Object,ArrayList<Boolean>> casa = new HashMap<Object,ArrayList<Boolean>>();

        for (int i = 1; i < (n/2) + 1; i++){
            g1[i] = eqs.get(i - 1);
            g2[i] = eqs.get(i + (n/2) - 1);
        }

        for (int j = 1; j < n; j++){

            JornadaTmp datos;

            for (int i = 1; i < (n/2) + 1; i++){
                Equipo eqCasa = calcularEqCasa(g1[i], g2[i], casa);
                
                if(casa.get(g1[i]) == null)
                    casa.put(g1[i], new ArrayList<Boolean>());
                if(casa.get(g2[i]) == null)
                    casa.put(g2[i], new ArrayList<Boolean>());
                
                if (g1[i].equals(eqCasa)){
                    datos = new JornadaTmp(j, g1[i], g2[i]);
                    casa.get(g1[i]).add(true);
                    casa.get(g2[i]).add(false);
                }
                else {
                    datos = new JornadaTmp(j, g2[i], g1[i]);
                    casa.get(g2[i]).add(true);
                    casa.get(g1[i]).add(false);
                }
                calendario.add(datos);
            }

            // Esto es para ir haciendo la rueda
            Equipo tmp1 = g1[n/2];
            Equipo tmp2 = g2[1];
            for (int i = n/2; i > 2 ; i--){
                g1[i] = g1[i - 1];
            }
            for (int i = 1 ; i < n/2; i++){
               g2[i] = g2[i + 1];
            }
            g1[2] = tmp2;
            g2[n/2] = tmp1;
        }

        // para la segunda vuelta invertimos
        // todos los partidos
        ArrayList calendario2 = new ArrayList();
        for (Object jorTmp : calendario) {
            JornadaTmp jor = (JornadaTmp) jorTmp;
            calendario2.add(new JornadaTmp(jor.getJornada() + n - 1,
                    jor.getEqVisitante(), jor.getEqLocal()));
        }

        calendario.addAll(calendario2);

        return calendario;

    }
    
    private static Equipo calcularEqCasa(Equipo eqA, Equipo eqB, 
            HashMap<Object, ArrayList<Boolean>> casa) {
        
        Equipo eqCasa = eqA;
        if (casa.isEmpty() || casa.get(eqA) == null || casa.get(eqB) == null )
            return eqCasa;
        
        ArrayList<Boolean> casaEqA = casa.get(eqA);
        ArrayList<Boolean> casaEqB = casa.get(eqB);
        int tamany = Math.min(casaEqA.size(), casaEqB.size()) ;
        
        if (tamany != 0)
            for (int i = tamany - 1; i >= 0; i--){
                if (casaEqA.get(i) == false && casaEqB.get(i) == true)
                    break;
                else if (casaEqA.get(i) == true && casaEqB.get(i) == false){
                    eqCasa = eqB;
                    break;
                }                
            }            
             
        return eqCasa;
        
    }

  
}

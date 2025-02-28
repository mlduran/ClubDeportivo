package mld.clubdeportivo.utilidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import mld.clubdeportivo.base.Equipo;

public class Calculos {

    	
    private Calculos() {
    }
    
    static private int getNum(int rangoInicial, int rangoFinal){
        
        var rnd = new Random();
        var num = (int) (rnd.nextDouble() * (rangoFinal - rangoInicial) + rangoInicial);
        
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

        var lista_val = new int[n_nums];
        var suma_total = media * n_nums;
        var ok = false;
        int x;

        while (ok == false)
        {
            var suma = 0;

            for (var i = 1; i < n_nums; i++)
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

        var vals = valoresAleatorios(n_nums, media,rango_min, rango_max);
        var listaVals = new ArrayList<Integer>();
        
        for (var i = 0; i < vals.length; i++) 
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

        var x = new int[n];

        for (var i = 0; i < n; i++){
            var nx = 0;
            var repetido = true;
            while(repetido){
                repetido = false;
                nx = getNum(rango_min, rango_max);
                for (var ii = 0; ii < i; ii++){
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

        var x = new int[n];

        for (var i = 0; i < n; i++){
            var nx = getNum(rango_min, rango_max);
            x[i] = nx;
        }

        return x;

    }

    static public boolean obtenerResultado(int valor1, int valor2)
    {// Devuelve true si aleatoriamente el valor1 supera al valor2
        boolean result;
        int v1 = 0, v2 = 0;

        for (var i = 0; i < 10; i++){
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


    
    public static List<JornadaTmp> crearCalendarioLiga(List<Equipo> eqs) {
        List<JornadaTmp> calendario = new ArrayList<>();
        Collections.shuffle(eqs); // Mezclar los equipos

        int n = eqs.size();
        Equipo[] g1 = new Equipo[(n / 2) + 1];
        Equipo[] g2 = new Equipo[(n / 2) + 1];

        Map<Equipo, List<Boolean>> casa = new HashMap<>();

        for (int i = 1; i <= n / 2; i++) {
            g1[i] = eqs.get(i - 1);
            g2[i] = eqs.get(i + (n / 2) - 1);
        }

        for (int j = 1; j < n; j++) {
            for (int i = 1; i <= n / 2; i++) {
                var eqCasa = calcularEqCasa(g1[i], g2[i], casa);

                casa.computeIfAbsent(g1[i], k -> new ArrayList<>());
                casa.computeIfAbsent(g2[i], k -> new ArrayList<>());

                if (g1[i].equals(eqCasa)) {
                    calendario.add(new JornadaTmp(j, g1[i], g2[i]));
                    casa.get(g1[i]).add(true);
                    casa.get(g2[i]).add(false);
                } else {
                    calendario.add(new JornadaTmp(j, g2[i], g1[i]));
                    casa.get(g2[i]).add(true);
                    casa.get(g1[i]).add(false);
                }
            }

            // Rotación de equipos
            var tmp1 = g1[n / 2];
            var tmp2 = g2[1];

            System.arraycopy(g1, 1, g1, 2, (n / 2) - 1);
            System.arraycopy(g2, 2, g2, 1, (n / 2) - 1);

            g1[2] = tmp2;
            g2[n / 2] = tmp1;
        }

        // Segunda vuelta: inversión de partidos
        List<JornadaTmp> calendario2 = new ArrayList<>();
        for (var jorTmp : calendario) {
            calendario2.add(new JornadaTmp(jorTmp.getJornada() + n - 1, jorTmp.getEqVisitante(), jorTmp.getEqLocal()));
        }

        calendario.addAll(calendario2);
        return calendario;
    }

    private static Equipo calcularEqCasa(Equipo eqA, Equipo eqB, Map<Equipo, List<Boolean>> casa) {
        if (casa.isEmpty() || !casa.containsKey(eqA) || !casa.containsKey(eqB)) {
            return eqA;
        }

        var casaEqA = casa.get(eqA);
        var casaEqB = casa.get(eqB);
        int tamany = Math.min(casaEqA.size(), casaEqB.size());

        for (int i = tamany - 1; i >= 0; i--) {
            if (!casaEqA.get(i) && casaEqB.get(i)) {
                break;
            } else if (casaEqA.get(i) && !casaEqB.get(i)) {
                return eqB;
            }
        }

        return eqA;
    }

  
}

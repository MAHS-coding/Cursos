package com.Microservicio.Cursos.utils;

import org.springframework.stereotype.Component;

@Component
public class RutUtils {
    public static boolean validarRut(String rut) {
        rut = rut.replace(".", "").replace("-", "").toUpperCase();
        if (rut.length() < 2) return false;
        
        String cuerpo = rut.substring(0, rut.length() - 1);
        String dv = rut.substring(rut.length() - 1);
        
        try {
            int cuerpoNum = Integer.parseInt(cuerpo);
            return calcularDigitoVerificador(cuerpoNum).equals(dv);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static String calcularDigitoVerificador(int rut) {
        int m = 0, s = 1;
        for (; rut != 0; rut /= 10) {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return s > 0 ? String.valueOf(s - 1) : "K";
    }
    
    public static String formatearRut(String rut) {
        rut = rut.replace(".", "").replace("-", "").toUpperCase();
        if (!validarRut(rut)) {
            throw new IllegalArgumentException("RUT inválido");
        }
        
        String cuerpo = rut.substring(0, rut.length() - 1);
        String dv = rut.substring(rut.length() - 1);
        
        StringBuilder rutFormateado = new StringBuilder();
        for (int i = 0; i < cuerpo.length(); i++) {
            if (i > 0 && (cuerpo.length() - i) % 3 == 0) {
                rutFormateado.append(".");
            }
            rutFormateado.append(cuerpo.charAt(i));
        }
        return rutFormateado.append("-").append(dv).toString();
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */
import enph.modelo.HogarDAO;
import enph.modelo.Usuario;
import enph.modelo.UsuarioDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InformeEncuestaController {

    private final UsuarioDAO usuarioDAO;
    private final HogarDAO hogarDAO;

    public InformeEncuestaController() {
        this.usuarioDAO = new UsuarioDAO();
        this.hogarDAO = new HogarDAO();
    }

    // Consultar únicamente los usuarios que son encuestadores
    public List<Usuario> listarEncuestadores() {

        List<Usuario> encuestadores = new ArrayList<>();

        List<Usuario> usuarios = usuarioDAO.listarUsuarios();

        for (Usuario usuario : usuarios) {

            if (usuario.getRol() != null
                    && usuario.getRol().trim().equalsIgnoreCase("ENCUESTADOR")
                    && usuario.getIdEncuestador() != null) {

                encuestadores.add(usuario);
            }
        }

        return encuestadores;
    }

    // Obtener las encuestas realizadas y pendientes por encuestador
    public Map<String, int[]> contarEncuestasPorEncuestador()
            throws SQLException {

        return hogarDAO.contarEncuestasPorEncuestador();
    }
}
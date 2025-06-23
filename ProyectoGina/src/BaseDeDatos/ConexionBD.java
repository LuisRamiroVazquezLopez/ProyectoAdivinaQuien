/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BaseDeDatos;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConexionBD {
    public static Connection conectar() {
        try {
            String url = "jdbc:mysql://localhost:3306/adivina_quien";
            String user = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean insertarPartida(PartidaBD juego) {
        String sql = "INSERT INTO partidas (jugador1, jugador2, ganador, `personaje ganador`, fecha, duracion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, juego.getJugador1());
            stmt.setString(2, juego.getJugador2());
            stmt.setString(3, juego.getGanador());
            stmt.setString(4, juego.getPersonajeGanador());
            stmt.setTimestamp(5, juego.getFechaSQL());
            stmt.setInt(6, juego.getDuracion());
            

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } 
    }
    
    public static List<PartidaBD> buscarPorNombre(String nombre) {
      List<PartidaBD> lista = new ArrayList();
      String sql = "SELECT * FROM partidas WHERE jugador1 LIKE ? OR jugador2 LIKE ?";

      try {
         Connection conn = conectar();

         try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            try {
               stmt.setString(1, "%" + nombre + "%");
               stmt.setString(2, "%" + nombre + "%");
               ResultSet rs = stmt.executeQuery();

               while(rs.next()) {
                   // Convertir Timestamp a LocalDateTime
                   java.sql.Timestamp timestamp = rs.getTimestamp("fecha");
                   LocalDateTime fecha = timestamp != null ? timestamp.toLocalDateTime() : null;
                  
                   PartidaBD partida = new PartidaBD(rs.getInt("id"), rs.getString("jugador1"), rs.getString("jugador2"), rs.getString("ganador"), rs.getString("personaje ganador"), fecha, rs.getInt("duracion"));
                   lista.add(partida);
                  
                  
                  
                  
               }
            } catch (Throwable var9) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (Throwable var10) {
            if (conn != null) {
               try {
                  conn.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }
            }

            throw var10;
         }

         if (conn != null) {
            conn.close();
         }
      } catch (Exception var11) {
         var11.printStackTrace();
      }

      return lista;
   }
    
    public static List<PartidaBD> buscarTodo() {
      List<PartidaBD> lista = new ArrayList();
      String sql = "SELECT * FROM partidas";

      try {
         Connection conn = conectar();

         try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            try {
               ResultSet rs = stmt.executeQuery();

               try {
                  while(rs.next()) {
                    java.sql.Timestamp timestamp = rs.getTimestamp("fecha");
                    LocalDateTime fecha = timestamp != null ? timestamp.toLocalDateTime() : null;

                    PartidaBD partida = new PartidaBD(rs.getInt("id"), rs.getString("jugador1"), rs.getString("jugador2"), rs.getString("ganador"), rs.getString("personaje ganador"), fecha, rs.getInt("duracion"));
                    lista.add(partida);
                  }
               } catch (Throwable var10) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var11) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var8) {
                     var11.addSuppressed(var8);
                  }
               }

               throw var11;
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (Throwable var12) {
            if (conn != null) {
               try {
                  conn.close();
               } catch (Throwable var7) {
                  var12.addSuppressed(var7);
               }
            }

            throw var12;
         }

         if (conn != null) {
            conn.close();
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      return lista;
   }
    
   public static List<PartidaBD> buscarOrdenado() {
      List<PartidaBD> lista = new ArrayList();
      String sql = "SELECT * FROM partidas ORDER BY duracion ASC";

      try {
         Connection conn = conectar();

         try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            try {
               ResultSet rs = stmt.executeQuery();

               try {
                  while(rs.next()) {
                    java.sql.Timestamp timestamp = rs.getTimestamp("fecha");
                    LocalDateTime fecha = timestamp != null ? timestamp.toLocalDateTime() : null;

                    PartidaBD partida = new PartidaBD(rs.getInt("id"), rs.getString("jugador1"), rs.getString("jugador2"), rs.getString("ganador"), rs.getString("personaje ganador"), fecha, rs.getInt("duracion"));
                    lista.add(partida);
                  }
               } catch (Throwable var10) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var11) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var8) {
                     var11.addSuppressed(var8);
                  }
               }

               throw var11;
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (Throwable var12) {
            if (conn != null) {
               try {
                  conn.close();
               } catch (Throwable var7) {
                  var12.addSuppressed(var7);
               }
            }

            throw var12;
         }

         if (conn != null) {
            conn.close();
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      return lista;
   }
    
   
   
    
}
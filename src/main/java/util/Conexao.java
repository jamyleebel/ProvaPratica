package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária para gerenciar conexões com o banco de dados 'empresa'.
 * Implementa o padrão Singleton para garantir uma única instância de conexão.
 */
public class Conexao {
    // Instância única da conexão
    private static Connection conexao = null;
    // URL do banco de dados MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/empresa?useSSL=false";
    // Nome de usuário do banco de dados (substitua pelo seu)
    private static final String USUARIO = "root";
    // Senha do banco de dados (substitua pela sua senha)
    private static final String SENHA = "";

    /**
     * Construtor privado para evitar instâncias externas.
     */
    private Conexao() {
        // Impede a criação de instâncias fora da classe
    }

    /**
     * Estabelece e retorna uma conexão com o banco de dados.
     * Reutiliza a conexão existente se ela estiver aberta.
     *
     * @return Objeto Connection para o banco de dados
     * @throws SQLException se ocorrer um erro de acesso ao banco
     */
    public static Connection getConexao() throws SQLException {
        // Verifica se a conexão é nula ou está fechada
        if (conexao == null || conexao.isClosed()) {
            try {
                // Carrega o driver JDBC do MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Estabelece a conexão com o banco
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                // Mensagem de sucesso (Regra de Negócio 5)
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            } catch (ClassNotFoundException e) {
                // Lança exceção com mensagem clara (Regra de Negócio 4)
                throw new SQLException("Driver JDBC não encontrado: " + e.getMessage());
            } catch (SQLException e) {
                // Lança exceção com mensagem clara (Regra de Negócio 4)
                throw new SQLException("Erro ao conectar ao banco de dados: " + e.getMessage());
            }
        }
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de dados, se estiver aberta.
     *
     * @throws SQLException se ocorrer um erro ao fechar a conexão
     */
    public static void fecharConexao() throws SQLException {
        // Verifica se a conexão existe e está aberta
        if (conexao != null && !conexao.isClosed()) {
            // Fecha a conexão
            conexao.close();
            // Mensagem de sucesso (Regra de Negócio 5)
            System.out.println("Conexão com o banco de dados fechada com sucesso!");
        }
    }
}
package dao;

import classe.Projeto;
import util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acesso a Dados (DAO) para gerenciar entidades Projeto no banco de dados.
 */
public class ProjetoDao {
    // Instância de FuncionarioDao para verificar existência de Funcionario
    private FuncionarioDao funcionarioDao = new FuncionarioDao();

    /**
     * Insere um novo Projeto no banco de dados.
     * Regra de Negócio 2: Verifica se o ID do Funcionario existe.
     *
     * @param projeto o Projeto a ser inserido
     * @throws SQLException se ocorrer um erro no banco ou se o ID do Funcionario não existir
     */
    public void inserir(Projeto projeto) throws SQLException {
        // Regra de Negócio 2: Verifica se o ID do Funcionario existe
        if (funcionarioDao.buscarPorId(projeto.getIdFuncionario()) == null) {
            throw new SQLException("Não é possível inserir projeto: Funcionário com ID " + projeto.getIdFuncionario() + " não existe.");
        }

        // Query SQL para inserir um projeto
        String sql = "INSERT INTO projeto (nome, descricao, id_funcionario) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Define os parâmetros da query
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setInt(3, projeto.getIdFuncionario());
            // Executa a inserção
            stmt.executeUpdate();
            // Obtém o ID gerado automaticamente
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    projeto.setId(rs.getInt(1));
                }
            }
            // Mensagem de sucesso (Regra de Negócio 5)
            System.out.println("Projeto inserido com sucesso: " + projeto);
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao inserir projeto: " + e.getMessage());
        }
    }

    /**
     * Atualiza um Projeto existente no banco de dados.
     * Regra de Negócio 2: Verifica se o ID do Funcionario existe.
     *
     * @param projeto o Projeto a ser atualizado
     * @throws SQLException se ocorrer um erro no banco ou se o ID do Funcionario não existir
     */
    public void atualizar(Projeto projeto) throws SQLException {
        // Regra de Negócio 2: Verifica se o ID do Funcionario existe
        if (funcionarioDao.buscarPorId(projeto.getIdFuncionario()) == null) {
            throw new SQLException("Não é possível atualizar projeto: Funcionário com ID " + projeto.getIdFuncionario() + " não existe.");
        }

        // Query SQL para atualizar um projeto
        String sql = "UPDATE projeto SET nome = ?, descricao = ?, id_funcionario = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define os parâmetros da query
            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setInt(3, projeto.getIdFuncionario());
            stmt.setInt(4, projeto.getId());
            // Executa a atualização
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Mensagem de sucesso (Regra de Negócio 5)
                System.out.println("Projeto atualizado com sucesso: " + projeto);
            } else {
                // Mensagem de erro (Regra de Negócio 4)
                throw new SQLException("Projeto com ID " + projeto.getId() + " não encontrado.");
            }
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao atualizar projeto: " + e.getMessage());
        }
    }

    /**
     * Exclui um Projeto do banco de dados.
     *
     * @param id o ID do Projeto a ser excluído
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public void excluir(int id) throws SQLException {
        // Query SQL para excluir um projeto
        String sql = "DELETE FROM projeto WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da query
            stmt.setInt(1, id);
            // Executa a exclusão
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Mensagem de sucesso (Regra de Negócio 5)
                System.out.println("Projeto com ID " + id + " excluído com sucesso.");
            } else {
                // Mensagem de erro (Regra de Negócio 4)
                throw new SQLException("Projeto com ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao excluir projeto: " + e.getMessage());
        }
    }

    /**
     * Busca um Projeto pelo ID.
     *
     * @param id o ID do Projeto
     * @return o objeto Projeto, ou null se não encontrado
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public Projeto buscarPorId(int id) throws SQLException {
        // Query SQL para buscar um projeto por ID
        String sql = "SELECT * FROM projeto WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da query
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Cria um objeto Projeto com os dados retornados
                    return new Projeto(rs.getInt("id"), rs.getString("nome"), rs.getString("descricao"),
                                      rs.getInt("id_funcionario"));
                }
            }
            return null; // Retorna null se não encontrar
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao buscar projeto: " + e.getMessage());
        }
    }

    /**
     * Lista todos os Projetos do banco de dados.
     *
     * @return uma lista de todos os Projetos
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public List<Projeto> listar() throws SQLException {
        // Lista para armazenar os projetos
        List<Projeto> projetos = new ArrayList<>();
        // Query SQL para listar todos os projetos
        String sql = "SELECT * FROM projeto";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            // Itera sobre os resultados
            while (rs.next()) {
                projetos.add(new Projeto(rs.getInt("id"), rs.getString("nome"), rs.getString("descricao"),
                                        rs.getInt("id_funcionario")));
            }
            return projetos;
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao listar projetos: " + e.getMessage());
        }
    }
}
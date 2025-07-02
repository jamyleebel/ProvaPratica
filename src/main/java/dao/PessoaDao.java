package dao;

import classe.Pessoa;
import util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acesso a Dados (DAO) para gerenciar entidades Pessoa no banco de dados.
 */
public class PessoaDao {
    /**
     * Insere uma nova Pessoa no banco de dados.
     *
     * @param pessoa a Pessoa a ser inserida
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public void inserir(Pessoa pessoa) throws SQLException {
        // Query SQL para inserir uma pessoa
        String sql = "INSERT INTO pessoa (nome, email) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Define os parâmetros da query
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getEmail());
            // Executa a inserção
            stmt.executeUpdate();
            // Obtém o ID gerado automaticamente
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pessoa.setId(rs.getInt(1));
                }
            }
            // Mensagem de sucesso (Regra de Negócio 5)
            System.out.println("Pessoa inserida com sucesso: " + pessoa);
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao inserir pessoa: " + e.getMessage());
        }
    }

    /**
     * Atualiza uma Pessoa existente no banco de dados.
     *
     * @param pessoa a Pessoa a ser atualizada
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public void atualizar(Pessoa pessoa) throws SQLException {
        // Query SQL para atualizar uma pessoa
        String sql = "UPDATE pessoa SET nome = ?, email = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define os parâmetros da query
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getEmail());
            stmt.setInt(3, pessoa.getId());
            // Executa a atualização
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Mensagem de sucesso (Regra de Negócio 5)
                System.out.println("Pessoa atualizada com sucesso: " + pessoa);
            } else {
                // Mensagem de erro (Regra de Negócio 4)
                throw new SQLException("Pessoa com ID " + pessoa.getId() + " não encontrada.");
            }
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao atualizar pessoa: " + e.getMessage());
        }
    }

    /**
     * Exclui uma Pessoa do banco de dados.
     *
     * @param id o ID da Pessoa a ser excluída
     * @throws SQLException se ocorrer um erro no banco ou se a Pessoa estiver vinculada a um Funcionário
     */
    public void excluir(int id) throws SQLException {
        // Verifica se a Pessoa está vinculada a um Funcionário
        String checkSql = "SELECT COUNT(*) FROM funcionario WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Mensagem de erro (Regra de Negócio 4)
                throw new SQLException("Não é possível excluir a pessoa com ID " + id + " pois está vinculada a um funcionário.");
            }
        }

        // Query SQL para excluir uma pessoa
        String sql = "DELETE FROM pessoa WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da query
            stmt.setInt(1, id);
            // Executa a exclusão
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Mensagem de sucesso (Regra de Negócio 5)
                System.out.println("Pessoa com ID " + id + " excluída com sucesso.");
            } else {
                // Mensagem de erro (Regra de Negócio 4)
                throw new SQLException("Pessoa com ID " + id + " não encontrada.");
            }
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao excluir pessoa: " + e.getMessage());
        }
    }

    /**
     * Busca uma Pessoa pelo ID.
     *
     * @param id o ID da Pessoa
     * @return o objeto Pessoa, ou null se não encontrado
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public Pessoa buscarPorId(int id) throws SQLException {
        // Query SQL para buscar uma pessoa por ID
        String sql = "SELECT * FROM pessoa WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da query
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Cria um objeto Pessoa com os dados retornados
                    return new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getString("email"));
                }
            }
            return null; // Retorna null se não encontrar
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao buscar pessoa: " + e.getMessage());
        }
    }

    /**
     * Lista todas as Pessoas do banco de dados.
     *
     * @return uma lista de todas as Pessoas
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public List<Pessoa> listar() throws SQLException {
        // Lista para armazenar as pessoas
        List<Pessoa> pessoas = new ArrayList<>();
        // Query SQL para listar todas as pessoas
        String sql = "SELECT * FROM pessoa";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            // Itera sobre os resultados
            while (rs.next()) {
                pessoas.add(new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getString("email")));
            }
            return pessoas;
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao listar pessoas: " + e.getMessage());
        }
    }
}
package dao;

import classe.Funcionario;
import util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de Acesso a Dados (DAO) para gerenciar entidades Funcionario no banco de dados.
 */
public class FuncionarioDao {
    // Instância de PessoaDao para verificar existência de Pessoa
    private PessoaDao pessoaDao = new PessoaDao();

    /**
     * Insere um novo Funcionario no banco de dados.
     * Regra de Negócio 1: Verifica se o ID da Pessoa existe.
     *
     * @param funcionario o Funcionario a ser inserido
     * @throws SQLException se ocorrer um erro no banco ou se o ID da Pessoa não existir
     */
    public void inserir(Funcionario funcionario) throws SQLException {
        // Regra de Negócio 1: Verifica se o ID da Pessoa existe
        if (pessoaDao.buscarPorId(funcionario.getId()) == null) {
            throw new SQLException("Não é possível inserir funcionário: Pessoa com ID " + funcionario.getId() + " não existe.");
        }

        // Query SQL para inserir um funcionário
        String sql = "INSERT INTO funcionario (id, matricula, departamento) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define os parâmetros da query
            stmt.setInt(1, funcionario.getId());
            stmt.setString(2, funcionario.getMatricula());
            stmt.setString(3, funcionario.getDepartamento());
            // Executa a inserção
            stmt.executeUpdate();
            // Mensagem de sucesso (Regra de Negócio 5)
            System.out.println("Funcionário inserido com sucesso: " + funcionario);
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao inserir funcionário: " + e.getMessage());
        }
    }

    /**
     * Atualiza um Funcionario existente no banco de dados.
     *
     * @param funcionario o Funcionario a ser atualizado
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public void atualizar(Funcionario funcionario) throws SQLException {
        // Query SQL para atualizar um funcionário
        String sql = "UPDATE funcionario SET matricula = ?, departamento = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define os parâmetros da query
            stmt.setString(1, funcionario.getMatricula());
            stmt.setString(2, funcionario.getDepartamento());
            stmt.setInt(3, funcionario.getId());
            // Executa a atualização
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Mensagem de sucesso (Regra de Negócio 5)
                System.out.println("Funcionário atualizado com sucesso: " + funcionario);
            } else {
                // Mensagem de erro (Regra de Negócio 4)
                throw new SQLException("Funcionário com ID " + funcionario.getId() + " não encontrado.");
            }
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao atualizar funcionário: " + e.getMessage());
        }
    }

    /**
     * Exclui um Funcionario do banco de dados.
     * Regra de Negócio 3: Proíbe a exclusão se o Funcionario estiver vinculado a um Projeto.
     *
     * @param id o ID do Funcionario a ser excluído
     * @throws SQLException se ocorrer um erro no banco ou se o Funcionario estiver vinculado a um Projeto
     */
    public void excluir(int id) throws SQLException {
        // Regra de Negócio 3: Verifica se o Funcionario está vinculado a um Projeto
        String checkSql = "SELECT COUNT(*) FROM projeto WHERE id_funcionario = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Mensagem de erro (Regra de Negócio 4)
                throw new SQLException("Não é possível excluir o funcionário com ID " + id + " pois está vinculado a um projeto.");
            }
        }

        // Query SQL para excluir um funcionário
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da query
            stmt.setInt(1, id);
            // Executa a exclusão
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Mensagem de sucesso (Regra de Negócio 5)
                System.out.println("Funcionário com ID " + id + " excluído com sucesso.");
            } else {
                // Mensagem de erro (Regra de Negócio 4)
                throw new SQLException("Funcionário com ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao excluir funcionário: " + e.getMessage());
        }
    }

    /**
     * Busca um Funcionario pelo ID, incluindo detalhes da Pessoa.
     *
     * @param id o ID do Funcionario
     * @return o objeto Funcionario, ou null se não encontrado
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public Funcionario buscarPorId(int id) throws SQLException {
        // Query SQL para buscar um funcionário com join na tabela pessoa
        String sql = "SELECT p.id, p.nome, p.email, f.matricula, f.departamento " +
                    "FROM funcionario f JOIN pessoa p ON f.id = p.id WHERE f.id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da query
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Cria um objeto Funcionario com os dados retornados
                    return new Funcionario(rs.getInt("id"), rs.getString("nome"), rs.getString("email"),
                                          rs.getString("matricula"), rs.getString("departamento"));
                }
            }
            return null; // Retorna null se não encontrar
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao buscar funcionário: " + e.getMessage());
        }
    }

    /**
     * Lista todos os Funcionarios do banco de dados, incluindo detalhes da Pessoa.
     *
     * @return uma lista de todos os Funcionarios
     * @throws SQLException se ocorrer um erro no banco de dados
     */
    public List<Funcionario> listar() throws SQLException {
        // Lista para armazenar os funcionários
        List<Funcionario> funcionarios = new ArrayList<>();
        // Query SQL para listar todos os funcionários com join na tabela pessoa
        String sql = "SELECT p.id, p.nome, p.email, f.matricula, f.departamento " +
                    "FROM funcionario f JOIN pessoa p ON f.id = p.id";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            // Itera sobre os resultados
            while (rs.next()) {
                funcionarios.add(new Funcionario(rs.getInt("id"), rs.getString("nome"), rs.getString("email"),
                                                rs.getString("matricula"), rs.getString("departamento")));
            }
            return funcionarios;
        } catch (SQLException e) {
            // Mensagem de erro (Regra de Negócio 4)
            throw new SQLException("Erro ao listar funcionários: " + e.getMessage());
        }
    }
}

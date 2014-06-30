package spca.datalayer.impl;

import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.SpcaDataLayerFactory;
import spca.datalayer.common.ProceduresNameConst;

public class DefaultDataBaseContext implements DataContext {

	private static final Logger logger = Logger
			.getLogger(SpcaDataLayerFactory.class);

	private final String url;
	private final String userName;
	private final String password;

	public DefaultDataBaseContext(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, userName, password);
	}

	private String buildSQLCallStatement(String procedureName, int paramCount) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("{? = call %s (", procedureName));

		for (int i = 0; i < paramCount; i++) {
			sb.append("?");

			if (i != paramCount - 1)
				sb.append(", ");
		}

		sb.append(")}");

		return sb.toString();
	}

	private String buildMultiParamString(Object[] array) {
		if (array == null || array.length == 0) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i].toString());

			if (i < array.length - 1) {
				sb.append(",");
			}
		}

		return sb.toString();
	}

	private DefaultDataResult executeQuery(String procedureName,
			Object... params) throws SQLException {
		Connection conn = null;
		CallableStatement cstat = null;

		try {
			conn = getConnection();
			cstat = conn.prepareCall(buildSQLCallStatement(procedureName,
					Array.getLength(params)));

			int paramIndex = 1;
			cstat.registerOutParameter(paramIndex++, Types.INTEGER);

			for (Object param : params) {
				cstat.setObject(paramIndex++, param);
			}

			cstat.executeQuery();

			return new DefaultDataResult(cstat);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (cstat != null)
				try {
					cstat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	private DefaultDataResult executeUpdate(String procedureName,
			Object... params) throws SQLException {
		Connection conn = null;
		CallableStatement cstat = null;

		try {
			conn = getConnection();
			cstat = conn.prepareCall(buildSQLCallStatement(procedureName,
					Array.getLength(params)));

			int paramIndex = 1;
			cstat.registerOutParameter(paramIndex++, Types.INTEGER);

			for (Object param : params) {
				cstat.setObject(paramIndex++, param);
			}

			cstat.executeUpdate();

			return new DefaultDataResult(cstat);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (cstat != null)
				try {
					cstat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public DataResult addAnimal(String name, String chipNum,
			Integer animalType, Integer sourceCity, Integer animalSource,
			Boolean specialNeeds) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_AddAnimal, name, chipNum,
				animalType, sourceCity, animalSource, specialNeeds);
	}

	@Override
	public DataResult addAnimalEventType(String name, Integer typeGroup)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_AddAnimalEventType,
				name, typeGroup);
	}

	@Override
	public DataResult addAnimalEventTypeGroup(String name) throws SQLException {
		return executeUpdate(
				ProceduresNameConst.Consts_AddAnimalEventTypeGroup, name);
	}

	@Override
	public DataResult addAnimalSource(String name) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_AddAnimalSource, name);
	}

	@Override
	public DataResult addAnimalType(String name) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_AddAnimalType, name);
	}

	@Override
	public DataResult addBalanceTarget(java.sql.Date startDate,
			java.sql.Date endDate, double amount, String name)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_AddBalanceTarget,
				startDate, endDate, amount, name);
	}

	@Override
	public DataResult addCity(String name) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_AddCity, name);
	}

	@Override
	public DataResult addContactType(String name, Integer contactTypeGroup)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_AddContactType, name,
				contactTypeGroup);
	}

	@Override
	public DataResult addContactTypeGroup(String name) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_AddContactTypeGroup,
				name);
	}

	@Override
	public DataResult addEvent(Integer eventType, Integer animalId,
			java.sql.Date dueDate, java.sql.Date date, String details,
			Integer contactInvolved) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_AddEvent, eventType,
				animalId, dueDate, date, details, contactInvolved);
	}

	@Override
	public DataResult addOrUpdateContact(String firstName, String lastName,
			String phone_1, String phone_2, String email_1, String email_2,
			String address, Integer city, String otherDetails,
			String identityCard, java.sql.Date birthDate) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_AddOrUpdateContact,
				firstName, lastName, phone_1, phone_2, email_1, email_2,
				address, city, otherDetails, identityCard, birthDate);
	}

	@Override
	public DataResult addPayment(Integer transactionId, Integer paymentType,
			Integer amount) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_AddPayment,
				transactionId, paymentType, amount);
	}

	@Override
	public DataResult addPaymentType(String name) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_AddPaymentType, name);
	}

	@Override
	public DataResult addSettings(Integer id, String name, String value)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Common_AddSettings, id, name,
				value);
	}

	@Override
	public DataResult addTransaction(Integer contactInvolved,
			Integer animalInvolved, Integer transactionType,
			Integer relatedEvent, String comments, String invoiceNumber)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_AddTransaction,
				contactInvolved, animalInvolved, transactionType, relatedEvent,
				comments, invoiceNumber);
	}

	@Override
	public DataResult addTransactionItem(String comments, Integer amount,
			Integer transactionID) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_AddTransactionItem,
				comments, amount, transactionID);
	}

	@Override
	public DataResult addTransactionType(String name) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_AddTransactionType,
				name);
	}

	@Override
	public DataResult addTypeForContact(Integer contactId, Integer contactType)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_AddTypeForContact,
				contactId, contactType);
	}

	@Override
	public DataResult deleteAnimalEventType(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_DeleteAnimalEventType,
				id);
	}

	@Override
	public DataResult deleteAnimalEventTypeGroup(Integer id)
			throws SQLException {
		return executeUpdate(
				ProceduresNameConst.Consts_DeleteAnimalEventTypeGroup, id);
	}

	@Override
	public DataResult deleteAnimalType(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_DeleteAnimalType, id);
	}

	@Override
	public DataResult deleteBalanceTarget(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_DeleteBalanceTarget, id);
	}

	@Override
	public DataResult deleteCities(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_DeleteCities, id);
	}

	@Override
	public DataResult deleteContactType(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_DeleteContactType, id);
	}

	@Override
	public DataResult deleteContactTypeGroup(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_DeleteContactTypeGroup,
				id);
	}

	@Override
	public DataResult deletePaymentType(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_DeletePaymentType, id);
	}

	@Override
	public DataResult deleteSettings(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.Common_DeleteSettings, id);
	}

	@Override
	public DataResult deleteTransactionType(Integer id) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_DeleteTransactionType,
				id);
	}

	@Override
	public DataResult deleteTypeForContact(Integer contactId,
			Integer contactType) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_DeleteTypeForContact,
				contactId, contactType);
	}

	@Override
	public DataResult getAllProcedures() throws SQLException {
		return executeQuery(ProceduresNameConst.System_GetAllProcedures);
	}

	@Override
	public DataResult getAnimalEventType() throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetAnimalEventType);
	}

	@Override
	public DataResult getAnimalEventTypeByGroup(Integer typeGroup)
			throws SQLException {
		return executeQuery(
				ProceduresNameConst.Consts_GetAnimalEventTypeByGroup, typeGroup);
	}

	@Override
	public DataResult getAnimalEventTypeGroups() throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetAnimalEventTypeGroups);
	}

	@Override
	public DataResult getAnimals(Integer[] animalIds, String animalName,
			Integer[] animalSources, Integer[] animalSourceCities,
			Integer[] animalTypes) throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetAnimals,
				buildMultiParamString(animalIds), animalName,
				buildMultiParamString(animalSources),
				buildMultiParamString(animalSourceCities),
				buildMultiParamString(animalTypes));
	}

	@Override
	public DataResult getAnimalTypes() throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetAnimalTypes);
	}

	@Override
	public DataResult getBalanceTarget(String ids, java.sql.Date startDate,
			java.sql.Date endDate) throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetBalanceTarget, ids,
				startDate, endDate);
	}

	@Override
	public DataResult getCities() throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetCities);
	}

	@Override
	public DataResult getContacts(Integer[] contactTypes,
			Integer[] contactTypeGroups, String contactName, Integer minAge)
			throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetContacts,
				buildMultiParamString(contactTypes),
				buildMultiParamString(contactTypeGroups), contactName, minAge);
	}

	@Override
	public DataResult getContactTypeGroups() throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetContactTypeGroups);
	}

	@Override
	public DataResult getContactTypes() throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetContactTypes);
	}

	@Override
	public DataResult getContactTypesByGroup(Integer contactTypeGroup)
			throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetContactTypesByGroup,
				contactTypeGroup);
	}

	@Override
	public DataResult getEvents(Integer[] eventTypes,
			Integer[] eventTypeGroups, Integer[] animalIds,
			java.sql.Date[] eventDates, Boolean openedEvents,
			java.sql.Date startDate, java.sql.Date endDate) throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetEvents,
				buildMultiParamString(eventTypes),
				buildMultiParamString(eventTypeGroups),
				buildMultiParamString(animalIds),
				buildMultiParamString(eventDates), openedEvents, startDate,
				endDate);
	}

	@Override
	public DataResult getItemsForTransaction(Integer transactionID)
			throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetItemsForTransaction,
				transactionID);
	}

	@Override
	public DataResult getMembersThatHasntPayed(Integer year)
			throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetMembersThatHasntPayed,
				year);
	}

	@Override
	public DataResult getPaymentsForTransaction(Integer transactionID)
			throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetPaymentsForTransaction,
				transactionID);
	}

	@Override
	public DataResult getPaymentsReports(Integer[] animalIds,
			java.sql.Date[] paymentDates, Integer[] contactsIds,
			Integer[] paymentTypes, Integer[] transactionTypes)
			throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetPaymentsReports,
				buildMultiParamString(animalIds),
				buildMultiParamString(paymentDates),
				buildMultiParamString(contactsIds),
				buildMultiParamString(paymentTypes),
				buildMultiParamString(transactionTypes));
	}

	@Override
	public DataResult getPaymentTypes() throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetPaymentTypes);
	}

	@Override
	public DataResult getProcedureParams(String procedureName)
			throws SQLException {
		return executeQuery(ProceduresNameConst.System_GetProcedureParams,
				procedureName);
	}

	@Override
	public DataResult getSetting(Integer id) throws SQLException {
		return executeQuery(ProceduresNameConst.Common_GetSetting, id);
	}

	@Override
	public DataResult getTransactions(Integer[] transactionIds,
			Integer[] transactionType, Integer[] eventTypes,
			Integer[] eventTypeGroups, Integer[] animalIds,
			java.sql.Date[] transactionDates, java.sql.Date startDate,
			java.sql.Date endDate, Boolean unpaiedTransaction)
			throws SQLException {
		return executeQuery(ProceduresNameConst.SPCA_GetTransactions,
				buildMultiParamString(transactionIds),
				buildMultiParamString(transactionType),
				buildMultiParamString(eventTypes),
				buildMultiParamString(eventTypeGroups),
				buildMultiParamString(animalIds),
				buildMultiParamString(transactionDates), startDate, endDate,
				unpaiedTransaction);
	}

	@Override
	public DataResult getTransactionType() throws SQLException {
		return executeQuery(ProceduresNameConst.Consts_GetTransactionType);
	}

	@Override
	public DataResult updateAnimal(Integer id, String name, String chipNum,
			Integer animalType, Integer sourceCity, Integer animalSource,
			Boolean specialNeeds) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_UpdateAnimal, id, name,
				chipNum, animalType, sourceCity, animalSource, specialNeeds);
	}

	@Override
	public DataResult updateAnimalEventType(Integer id, String name)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_UpdateAnimalEventType,
				id, name);
	}

	@Override
	public DataResult updateAnimalEventTypeGroup(Integer id, String name)
			throws SQLException {
		return executeUpdate(
				ProceduresNameConst.Consts_UpdateAnimalEventTypeGroup, id, name);
	}

	@Override
	public DataResult updateAnimalType(Integer id, String name)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_UpdateAnimalType, id,
				name);
	}

	@Override
	public DataResult updateBalanceTarget(Integer id, java.sql.Date startDate,
			java.sql.Date endDate, double amount, String name)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_UpdateBalanceTarget, id,
				startDate, endDate, amount, name);
	}

	@Override
	public DataResult updateCities(Integer id, String name) throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_UpdateCities, id, name);
	}

	@Override
	public DataResult updateContactType(Integer id, String name)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_UpdateContactType, id,
				name);
	}

	@Override
	public DataResult updateContactTypeGroup(Integer id, String name)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_UpdateContactTypeGroup,
				id, name);
	}

	@Override
	public DataResult updateEvent(Integer id, Integer eventType,
			Integer animalId, java.sql.Date dueDate, java.sql.Date date,
			String details, Integer contactInvolved) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_UpdateEvent, id,
				eventType, animalId, dueDate, date, details, contactInvolved);
	}

	@Override
	public DataResult updatePayment(Integer id, Integer transactionId,
			Integer paymentType, Integer amount) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_UpdatePayment, id,
				transactionId, paymentType, amount);
	}

	@Override
	public DataResult updatePaymentType(Integer id, String name)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_UpdatePaymentType, id,
				name);
	}

	@Override
	public DataResult updateSetting(Integer id, String value)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Common_UpdateSetting, id,
				value);
	}

	@Override
	public DataResult updateSettings(Integer id, String name, String value)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Common_UpdateSettings, id,
				name, value);
	}

	@Override
	public DataResult updateSTransactionType(Integer id, String value)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.Consts_UpdateSTransactionType,
				id, value);
	}

	@Override
	public DataResult updateTransaction(Integer id, Integer contactInvolved,
			Integer animalInvolved, Integer transactionType,
			Integer relatedEvent, String comments, String invoiceNumber)
			throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_UpdateTransaction, id,
				contactInvolved, animalInvolved, transactionType, relatedEvent,
				comments, invoiceNumber);
	}

	@Override
	public DataResult updateTransactionItem(Integer id, String comments,
			Integer amount, Integer transactionID) throws SQLException {
		return executeUpdate(ProceduresNameConst.SPCA_UpdateTransactionItem,
				id, comments, amount, transactionID);
	}
}

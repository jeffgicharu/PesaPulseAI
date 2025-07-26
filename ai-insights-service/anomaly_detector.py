from collections import defaultdict
import datetime

def detect_anomalies(transactions: list, user_id: str) -> list:
    """
    Analyzes a list of transactions to detect spending anomalies.

    An anomaly is defined as any single transaction whose amount is more than
    2.5 times the average transaction amount for its category.

    Args:
        transactions: A list of transaction dictionaries.
                      Should have 'category' and 'amount' keys.
        user_id: The ID of the user these transactions belong to.

    Returns:
        A list of insight dictionaries, where each dictionary represents
        a detected anomaly ready to be stored in MongoDB.
    """
    if not transactions:
        return []

    # --- Step 1: Calculate category totals and counts ---
    category_totals = defaultdict(float)
    category_counts = defaultdict(int)

    for t in transactions:
        # We only analyze expenses (negative amounts)
        if t['amount'] < 0:
            category = t.get('category', 'Uncategorized')
            # Use absolute value for calculations
            amount = abs(t['amount'])
            category_totals[category] += amount
            category_counts[category] += 1

    # --- Step 2: Calculate category averages ---
    category_averages = {}
    for category, total in category_totals.items():
        count = category_counts[category]
        if count > 1: # Only calculate average if there's more than one transaction
            category_averages[category] = total / count
        else:
            # If only one transaction, we can't detect an anomaly against an average
            category_averages[category] = total 

    # --- Step 3: Identify anomalies ---
    insights = []
    anomaly_threshold_multiplier = 2.5

    for t in transactions:
        if t['amount'] < 0:
            category = t.get('category', 'Uncategorized')
            amount = abs(t['amount'])
            average = category_averages.get(category)

            if average and amount > (average * anomaly_threshold_multiplier):
                # Anomaly detected! Create an insight object.
                insight = {
                    "userId": user_id,
                    "generatedAt": datetime.datetime.now(datetime.timezone.utc),
                    "type": "SPENDING_ANOMALY",
                    "severity": "Warning",
                    "title": f"Unusual Spending in '{category}'",
                    "message": f"A transaction of KES {amount:,.2f} for '{t['details']}' is significantly higher than your average of KES {average:,.2f} for this category.",
                    "relatedTransactionIds": [t['transactionId']]
                }
                insights.append(insight)

    return insights
